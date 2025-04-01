import { useQuery, UseQueryResult } from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import { MissionAction } from '../types/mission-action'
import { actionsKeys } from './query-keys.ts'
import queryClient from '../../../../query-client'

export const fetchAction = ({ missionId, actionId }: { missionId: string; actionId: string }): Promise<MissionAction> =>
  axios.get(`missions/${missionId}/actions/${actionId}`).then(response => response.data)

const useGetActionQuery = (missionId: number, actionId?: string): UseQueryResult<MissionAction, Error> => {
  queryClient.setQueryDefaults(['actions'], {
    queryFn: ({ queryKey }) => {
      const actionId = queryKey[1]
      if (typeof actionId !== 'string') {
        throw new Error('actionId missing in queryKey')
      }
      // You need to pass the `missionId` here somehow — maybe it's encoded in the actionId?
      return fetchAction({ missionId, actionId })
    }
  })

  const query = useQuery<MissionAction>({
    queryKey: actionsKeys.byId(actionId),
    enabled: !!actionId,
    queryFn: data => fetchAction({ missionId, actionId })
  })
  return query
}

export default useGetActionQuery
