import { ApolloError, FetchPolicy, gql, useQuery } from '@apollo/client'
import { Mission } from '../../../common/types/mission-types.ts'
import { WatchQueryFetchPolicy } from '@apollo/client/core/watchQueryOptions'

export const GET_MISSION_EXCERPT = gql`
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

const useMissionExcerpt = (
  missionId?: string,
  fetchPolicy: WatchQueryFetchPolicy = 'cache-first'
): {
  data?: Mission
  loading: boolean
  error?: ApolloError
} => {
  const { loading, error, data } = useQuery(GET_MISSION_EXCERPT, {
    variables: { missionId },
    fetchPolicy: fetchPolicy
  })

  return { loading, error, data: data?.mission }
}

export default useMissionExcerpt
