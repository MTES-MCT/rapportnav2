import { useQuery } from '@tanstack/react-query'

import axios from '../../../../query-client/axios.ts'
import { MissionCrew } from '../types/crew-type.ts'
import { missionCrewKeys } from './query-keys.ts'

const useMissionCrewQuery = (missionId?: string) => {
  const fetchMissionCrew = (): Promise<MissionCrew[]> =>
    axios.get(`crews/mission/${missionId}`).then(response => response.data)

  const query = useQuery<MissionCrew[], Error>({
    queryKey: missionCrewKeys.byMissionId(missionId),
    queryFn: fetchMissionCrew,
    retry: 2 // Retry failed requests twice before throwing an error
  })
  return query
}

export default useMissionCrewQuery
