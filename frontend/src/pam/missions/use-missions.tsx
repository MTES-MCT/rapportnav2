import { ApolloClient, ApolloError, gql, useQuery } from '@apollo/client'
import { Mission } from "../../types/mission-types.ts";

export const GET_MISSIONS = gql`
  query GetMissions {
    missions {
      id
      missionSource
      startDateTimeUtc
      endDateTimeUtc
      openBy
      status
      reportStatus {
        status
        source
      }
    }
  }
`


const useMissions = (): {
  data?: Mission[];
  loading: boolean;
  error?: ApolloError,
  client: ApolloClient<any>
} => {
  const {loading, error, data, ...rest} = useQuery(GET_MISSIONS, {
    // fetchPolicy: 'cache-only'
  })

  return {loading, error, data: data?.missions, ...rest}
}

export default useMissions
