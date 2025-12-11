import { skipToken, useQuery, useQueryClient } from '@tanstack/react-query'
import { DYNAMIC_DATA_STALE_TIME } from '../../../../query-client'
import axios from '../../../../query-client/axios'
import { Mission2 } from '../types/mission-types'
import { actionsKeys, missionsKeys } from './query-keys.ts'
import { useOnlineManager } from '../hooks/use-online-manager.tsx'
import { MissionAction } from '../types/mission-action.ts'

export const fetchMission = (missionId?: string): Promise<Mission2> =>
  axios.get(`missions/${missionId}`).then(response => {
    return response.data
  })

const useGetMissionQuery = (missionId?: string) => {
  const queryClient = useQueryClient()
  const { isOnline } = useOnlineManager()

  return useQuery<Mission2>({
    queryKey: missionsKeys.byId(missionId),
    queryFn: missionId
      ? async () => {
          const mission = await fetchMission(missionId)
          // prefill individual cache keys
          mission.actions?.forEach((action: MissionAction) => {
            queryClient.setQueryData(actionsKeys.byId(action.id), action)
          })
          return mission
        }
      : skipToken,
    staleTime: DYNAMIC_DATA_STALE_TIME, // Cache data for 5 minutes
    retry: 2, // Retry failed requests twice before throwing an error
    refetchInterval: DYNAMIC_DATA_STALE_TIME,
    enabled: isOnline
  })
}

export default useGetMissionQuery
