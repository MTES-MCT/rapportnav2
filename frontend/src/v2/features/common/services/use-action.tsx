import { useQuery, UseQueryResult } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { MissionAction } from '../types/mission-action.ts'
import { actionsKeys } from './query-keys.ts'

export const fetchAction = ({ ownerId, actionId }: { ownerId: string; actionId: string }): Promise<MissionAction> =>
  axios.get(`owners/${ownerId}/actions/${actionId}`).then(response => response.data)

const useGetActionQuery = (ownerId?: string, actionId?: string): UseQueryResult<MissionAction, Error> => {
  const query = useQuery<MissionAction>({
    queryKey: actionsKeys.byId(actionId),
    enabled: !!actionId,
    queryFn: data => fetchAction({ ownerId, actionId }),
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false
  })
  return query
}

export default useGetActionQuery
