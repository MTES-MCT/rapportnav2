import { ApolloError, gql, useQuery } from '@apollo/client'
import { Mission } from '@common/types/mission-types'

export const GET_ULAM_MISSION_EXCERPT = gql`
  query GetMissionExcerpt($missionId: ID) {
    mission(missionId: $missionId) {
      id
      missionSource
      startDateTimeUtc
      endDateTimeUtc
      status
      completenessForStats {
        status
        sources
      }
      generalInfo {
        id
        distanceInNauticalMiles
        consumedGOInLiters
        consumedFuelInLiters
        nbrOfRecognizedVessel
        serviceId
      }
      actions {
        id
        type
        source
        status
        completenessForStats {
          status
          sources
        }
      }
      services {
        id
        name
      }
      observationsByUnit
    }
  }
`

export const useMissionExcerptQuery = (
  missionId?: string
): { data?: Mission; loading: boolean; error?: ApolloError } => {
  const { loading, error, data } = useQuery(GET_ULAM_MISSION_EXCERPT, {
    variables: { missionId }
    // fetchPolicy: 'cache-only'
  })

  return { loading, error, data: data?.mission }
}
