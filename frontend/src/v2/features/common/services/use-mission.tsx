import { skipToken, useQuery, useQueryClient } from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import { Mission2 } from '../types/mission-types'
import { missionsKeys } from './query-keys.ts'
import { DYNAMIC_DATA_STALE_TIME } from '../../../../query-client'

export const fetchMission = (missionId: string): Promise<Mission2> =>
  axios.get(`missions/${missionId}`).then(response => {
    return response.data
  })
const useGetMissionQuery = (missionId?: string) => {
  const queryClient = useQueryClient()

  const query = useQuery<Mission2>({
    queryKey: missionsKeys.byId(missionId),
    queryFn: missionId ? () => fetchMission(missionId) : skipToken,
    staleTime: DYNAMIC_DATA_STALE_TIME, // Cache data for 5 minutes
    retry: 2, // Retry failed requests twice before throwing an error
    initialDataUpdatedAt: () => queryClient.getQueryState(missionsKeys.byId(missionId))?.dataUpdatedAt // used in footer to show "last sync at xxx" message
  })

  return query
}

export default useGetMissionQuery
