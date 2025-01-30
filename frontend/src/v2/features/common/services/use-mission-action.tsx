import { useQuery, UseQueryResult } from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import { MissionAction } from '../types/mission-action'
import { actionKeys } from '../../../../query-client/query-keys.tsx'

const useGetActionQuery = (missionId: number, actionId?: string): UseQueryResult<MissionAction, Error> => {
  const fetchAction = (): Promise<MissionAction> =>
    axios.get(`missions/${missionId}/actions/${actionId}`).then(response => response.data)

  const query = useQuery<MissionAction>({
    queryKey: actionKeys.detail(actionId),
    queryFn: fetchAction,
    enabled: !!actionId,
    networkMode: 'offlineFirst',
    staleTime: 2 * 60 * 1000
  })
  return query
}

export default useGetActionQuery
