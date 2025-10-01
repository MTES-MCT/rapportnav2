import { useQuery, useQueryClient, UseQueryResult } from '@tanstack/react-query'
import { DYNAMIC_DATA_STALE_TIME } from '../../../../query-client'
import axios from '../../../../query-client/axios.ts'
import { Mission2 } from '../types/mission-types.ts'
import { actionsKeys, missionsKeys } from './query-keys.ts'
import { useEffect } from 'react'
import { MissionAction } from '../types/mission-action.ts'
import { useOnlineManager } from '../hooks/use-online-manager.tsx'

const useMissionsQuery = (params: URLSearchParams): UseQueryResult<Mission2[], Error> => {
  const queryClient = useQueryClient()
  const { isOnline } = useOnlineManager()

  const fetchMissions = async (): Promise<Mission2[]> => {
    const response = await axios.get<Mission2[]>(`missions?${params.toString()}`)
    return response.data
  }

  const endDateTimeUtc = params.get('endDateTimeUtc')
  const startDateTimeUtc = params.get('startDateTimeUtc')

  const query = useQuery<Mission2[], Error>({
    queryKey: missionsKeys.filter(JSON.stringify({ startDateTimeUtc, endDateTimeUtc })),
    queryFn: fetchMissions,
    enabled: !!endDateTimeUtc && !!startDateTimeUtc && isOnline, // Prevents query from running if startDateTimeUtc is not provided
    staleTime: DYNAMIC_DATA_STALE_TIME, // Cache data for 3 minutes
    retry: 2, // Retry failed requests twice before throwing an error,
    refetchInterval: DYNAMIC_DATA_STALE_TIME
  })

  useEffect(() => {
    if (!query.data) return
    else {
      // for offline mode, preset the mission in their individual cache keys
      ;(query.data || []).forEach((mission: Mission2) => {
        queryClient.setQueryData(missionsKeys.byId(mission.id), mission)
        ;(mission.actions || []).forEach((action: MissionAction) => {
          queryClient.setQueryData(actionsKeys.byId(action.id), action)
        })
      })
    }
  }, [query.data, queryClient])

  return query
}

export default useMissionsQuery
