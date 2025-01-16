import {skipToken, useQuery, UseQueryResult} from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import {MissionAction} from '../types/mission-action'

const useGetActionQuery = (missionId: number, actionId?: string): UseQueryResult<MissionAction, Error> => {
  const fetchAction = (): Promise<MissionAction> =>
    axios.get(`missions/${missionId}/actions/${actionId}`).then(response => response.data)

  const query = useQuery<MissionAction>({
    queryKey: ['action', actionId],
    queryFn: actionId ? fetchAction : skipToken
  })
  return query
}

export default useGetActionQuery
