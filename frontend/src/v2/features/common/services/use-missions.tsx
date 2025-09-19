import { useQueries, useQueryClient, UseQueryResult } from '@tanstack/react-query'
import { DYNAMIC_DATA_STALE_TIME, HOURLY_TIME, STATIC_DATA_STALE_TIME } from '../../../../query-client'
import axios from '../../../../query-client/axios.ts'
import { Mission2 } from '../types/mission-types.ts'
import { actionsKeys, missionsKeys } from './query-keys.ts'
import { MissionAction } from '../types/mission-action.ts'
import { startOfMonth, endOfMonth, startOfYear, eachMonthOfInterval, endOfYear } from 'date-fns'
import { fetchAction } from './use-action.tsx'
import { fetchMission } from './use-mission.tsx'
import { useOnlineManager } from '../hooks/use-online-manager.tsx'

export type Frame = 'monthly' | 'yearly'

const fetchMissions = async (start: Date, end: Date): Promise<Mission2[]> => {
  const params = new URLSearchParams({
    startDateTimeUtc: start.toISOString(),
    endDateTimeUtc: end.toISOString()
  })
  const response = await axios.get<Mission2[]>(`missions?${params.toString()}`)
  return response.data
}

// add cache for each mission and action
const normalizeMission = (queryClient: ReturnType<typeof useQueryClient>, mission: Mission2) => {
  queryClient.setQueryData(missionsKeys.byId(mission.id), mission)
  // ensureQueryData so that cache-key can auto-refresh with refetchInterval
  queryClient.ensureQueryData({
    queryKey: missionsKeys.byId(mission.id),
    queryFn: () => fetchMission(mission.id)
  })
  mission.actions?.forEach((action: MissionAction) => {
    queryClient.setQueryData(actionsKeys.byId(action.id), action)
    // ensureQueryData so that cache-key can auto-refresh with refetchInterval
    queryClient.ensureQueryData({
      queryKey: actionsKeys.byId(action.id),
      queryFn: () => fetchAction({ ownerId: mission.id, actionId: action.id })
    })
  })
}

// get all months till the beginning of the year
// for previous years, get all months
const getMonthsForYear = (year: number): { start: Date; end: Date; isCurrent: boolean }[] => {
  const now = new Date()
  const currentMonth = now.getUTCMonth()
  const currentYear = now.getUTCFullYear()

  const yearStart = startOfYear(new Date(Date.UTC(year, 0, 1)))

  // For current year, only go up to current month. For past years, full year.
  const yearEnd = year === currentYear ? endOfMonth(now) : endOfMonth(new Date(Date.UTC(year, 11, 31)))

  return eachMonthOfInterval({ start: yearStart, end: yearEnd })
    .map(monthStart => ({
      start: startOfMonth(monthStart),
      end: endOfMonth(monthStart),
      isCurrent: year === currentYear && monthStart.getUTCMonth() === currentMonth
    }))
    .reverse() // Start with most recent months first
}

const useMissionsQuery = (params: URLSearchParams, frame: Frame = 'monthly'): UseQueryResult<Mission2[], Error> => {
  const queryClient = useQueryClient()
  const { isOnline } = useOnlineManager()

  const now = new Date()

  const startParam = params.get('startDateTimeUtc') ?? startOfYear(now)
  const endParam = params.get('endDateTimeUtc') ?? endOfYear(now)

  const startDate = new Date(startParam)
  const endDate = new Date(endParam)

  // For yearly frame, use the end date year as the target year (due to timezone handling)
  const targetYear = endDate.getUTCFullYear()
  const currentYear = now.getUTCFullYear()

  // Determine queries to make
  const queries =
    frame === 'monthly' || targetYear > currentYear
      ? [{ start: startDate, end: endDate, isCurrent: frame === 'monthly' }]
      : getMonthsForYear(targetYear)

  // Single useQueries for all scenarios
  const results = useQueries({
    queries: queries.map(({ start, end, isCurrent }) => ({
      queryKey: missionsKeys.filter(
        JSON.stringify({ startDateTimeUtc: start.toISOString(), endDateTimeUtc: end.toISOString() })
      ),
      queryFn: () => fetchMissions(start, end),
      staleTime: isCurrent ? DYNAMIC_DATA_STALE_TIME : STATIC_DATA_STALE_TIME,
      gcTime: isCurrent ? DYNAMIC_DATA_STALE_TIME : STATIC_DATA_STALE_TIME,
      retry: 2,
      revalidateIfStale: isCurrent,
      refetchInterval: isCurrent ? HOURLY_TIME : false,
      enabled: isOnline,
      select: (missions: Mission2[]) => {
        missions.forEach(m => normalizeMission(queryClient, m))
        return missions
      }
    }))
  })

  // Combine all missions
  const allMissions = results.flatMap(query => query.data ?? [])

  return {
    ...results[0],
    data: allMissions
  } as UseQueryResult<Mission2[], Error>
}

export default useMissionsQuery
