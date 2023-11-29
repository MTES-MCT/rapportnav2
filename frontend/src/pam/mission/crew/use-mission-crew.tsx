import { ApolloError, useQuery } from '@apollo/client'
import { GET_MISSION_CREW } from '../queries'
import { MissionCrew } from '../../../types/crew-types'

const useMissionCrew = (missionId: string): { data?: MissionCrew[]; loading: boolean; error?: ApolloError } => {
  const { loading, error, data } = useQuery(GET_MISSION_CREW, {
    variables: { missionId }
    // fetchPolicy: 'cache-only'
  })

  return { loading, error, data: data?.missionCrewByMissionId }
}

export default useMissionCrew
