import { useQuery, UseQueryResult } from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import { MissionAction } from '../types/mission-action'
import { actionsKeys } from './query-keys.ts'

const useGetActionQuery = (missionId?: string, actionId?: string): UseQueryResult<MissionAction, Error> => {
  const fetchAction = (): Promise<MissionAction> =>
    axios.get(`missions/${missionId}/actions/${actionId}`).then(response => response.data)

  const query = useQuery<MissionAction>({
    queryKey: actionsKeys.byId(actionId),
    enabled: !!actionId && !!missionId,
    queryFn: fetchAction
  })
  return query
}

export default useGetActionQuery
