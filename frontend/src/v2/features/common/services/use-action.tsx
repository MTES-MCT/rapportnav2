import { useQuery, UseQueryResult } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { MissionAction } from '../types/mission-action.ts'
import { actionsKeys } from './query-keys.ts'

const useGetActionQuery = (ownerId?: string, actionId?: string): UseQueryResult<MissionAction, Error> => {
  const fetchAction = (): Promise<MissionAction> =>
    axios.get(`owners/${ownerId}/actions/${actionId}`).then(response => response.data)

  const query = useQuery<MissionAction>({
    queryKey: actionsKeys.byId(actionId),
    enabled: !!actionId,
    queryFn: fetchAction,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false
  })
  return query
}

export default useGetActionQuery
