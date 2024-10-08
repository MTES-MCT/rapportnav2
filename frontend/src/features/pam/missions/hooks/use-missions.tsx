import { ApolloClient, ApolloError, gql, useQuery } from '@apollo/client'
import { Mission } from '../../../common/types/mission-types.ts'

export const GET_MISSIONS = gql`
  query GetMissions {
    missions {
      id
      missionSource
      startDateTimeUtc
      endDateTimeUtc
      openBy
      status
      completenessForStats {
        status
        sources
      }
    }
  }
`

const useMissions = (): {
  data?: Mission[]
  loading: boolean
  error?: ApolloError
  client: ApolloClient<any>
} => {
  const { loading, error, data, ...rest } = useQuery(GET_MISSIONS, {
    fetchPolicy: 'cache-and-network'
  })

  return { loading, error, data: data?.missions, ...rest }
}

export default useMissions
