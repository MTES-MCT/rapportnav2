import { skipToken, useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import { Mission2 } from '../types/mission-types'

const useGetMissionQuery = (missionId?: string) => {
  const fetchMission = (): Promise<Mission2> => axios.get(`missions/${missionId}`).then(response => response.data)

  const query = useQuery<Mission2>({
    queryKey: ['mission', missionId],
    queryFn: missionId ? fetchMission : skipToken
  })
  return query
}

export default useGetMissionQuery
