import { ApolloError, gql, useQuery } from '@apollo/client'
import { MissionCrew } from '../../../types/crew-types'

export const GET_MISSION_CREW = gql`
  query GetMissionCrewByMissionId($missionId: ID!) {
    missionCrewByMissionId(missionId: $missionId) {
      id
      agent {
        id
        firstName
        lastName
      }
      role {
        id
        title
      }
      comment
    }
  }
`
const useMissionCrew = (missionId: string): { data?: MissionCrew[]; loading: boolean; error?: ApolloError } => {
  const { loading, error, data } = useQuery(GET_MISSION_CREW, {
    variables: { missionId }
    // fetchPolicy: 'cache-only'
  })

  return { loading, error, data: data?.missionCrewByMissionId }
}

export default useMissionCrew
