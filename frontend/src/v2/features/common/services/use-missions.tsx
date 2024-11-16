import { ApolloClient, ApolloError, gql, useQuery } from '@apollo/client'
import { Mission } from '../../../common/types/mission-types.ts'

export const GET_MISSIONS = gql`
  query GetMissions($envInput: MissionsFetchEnvInput!) {
    missionsV2(envInput: $envInput) {
      id
      missionSource
      startDateTimeUtc
      endDateTimeUtc
      openBy
      status
      observationsByUnit
      completenessForStats {
        status
        sources
      }
      crew {
        id
        agent {
          id
          firstName
          lastName
        }
      }
    }
  }
`

interface UseMissionsQueryParams {
  startDateTimeUtc: string
  endDateTimeUtc: string
}

const useMissionsQuery = ({ startDateTimeUtc, endDateTimeUtc }: UseMissionsQueryParams): {
  data?: Mission[]
  loading: boolean
  error?: ApolloError
  client: ApolloClient<any>
} => {
  const { loading, error, data, ...rest } = useQuery(GET_MISSIONS, {
    variables: { envInput: { startDateTimeUtc, endDateTimeUtc } },
    fetchPolicy: 'cache-and-network'
  })

  return { loading, error, data: data?.missionsV2, ...rest }
}

export default useMissionsQuery
