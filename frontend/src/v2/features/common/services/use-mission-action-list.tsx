import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import { MissionAction } from '../types/mission-action'

const useGetActionListQuery = (missionId: number) => {
  const fetchActions = (): Promise<MissionAction[]> =>
    axios.get(`missions/${missionId}/actions`).then(response => response.data)

  const query = useQuery<MissionAction[]>({
    queryKey: ['actions', missionId],
    queryFn: fetchActions
  })
  return query
}

export default useGetActionListQuery
