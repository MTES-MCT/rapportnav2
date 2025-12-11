import { useQuery, useQueryClient, UseQueryResult } from '@tanstack/react-query'
import { DYNAMIC_DATA_STALE_TIME } from '../../../../query-client'
import axios from '../../../../query-client/axios.ts'
import { Mission2 } from '../types/mission-types.ts'
import { actionsKeys, missionsKeys } from './query-keys.ts'
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

  return useQuery<Mission2[], Error>({
    queryKey: missionsKeys.filter(JSON.stringify({ startDateTimeUtc, endDateTimeUtc })),
    queryFn: async () => {
      const missions = await fetchMissions()
      // prefill individual cache keys
      ;(missions || []).forEach((mission: Mission2) => {
        queryClient.setQueryData(missionsKeys.byId(mission.id), mission)
        ;(mission.actions || []).forEach((action: MissionAction) => {
          queryClient.setQueryData(actionsKeys.byId(action.id), action)
        })
      })
      return missions
    },
    enabled: !!endDateTimeUtc && !!startDateTimeUtc && isOnline, // Prevents query from running if startDateTimeUtc is not provided
    staleTime: DYNAMIC_DATA_STALE_TIME, // Cache data for 3 minutes
    retry: 2, // Retry failed requests twice before throwing an error,
    refetchInterval: DYNAMIC_DATA_STALE_TIME
  })
}

export default useMissionsQuery
