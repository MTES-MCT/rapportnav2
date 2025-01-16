import { skipToken, useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import { Mission } from '../types/mission-types'

const useGetMissionQuery = (missionId?: string) => {
  const fetchMission = (): Promise<Mission> => axios.get(`missions/${missionId}`).then(response => response.data)

  const query = useQuery<Mission>({
    queryKey: ['mission', missionId],
    queryFn: missionId ? fetchMission : skipToken
  })
  return query
}

export default useGetMissionQuery
