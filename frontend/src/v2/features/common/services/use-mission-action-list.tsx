import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import { MissionAction } from '../types/mission-action'
import { actionKeys } from '../../../../query-client/query-keys.tsx'

export const QUERY_KEY_GET_ACTIONS = ['actions']

const useGetActionListQuery = (missionId: number) => {
  const fetchActions = (): Promise<MissionAction[]> =>
    axios.get(`missions/${missionId}/actions`).then(response => response.data)

  const query = useQuery<MissionAction[]>({
    queryKey: actionKeys.all(),
    queryFn: fetchActions,
    networkMode: 'offlineFirst',
    staleTime: 2 * 60 * 1000
  })
  debugger
  return query
}

export default useGetActionListQuery
