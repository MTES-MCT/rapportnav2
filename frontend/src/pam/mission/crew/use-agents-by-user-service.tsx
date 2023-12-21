import { ApolloError, gql, useQuery } from '@apollo/client'
import { Agent } from '../../../types/crew-types'

export const GET_AGENTS_BY_USER_SERVICE = gql`
  query GetAgentsByUserService {
    agentsByUserService {
      id
      firstName
      lastName
    }
  }
`

const useAgentsByUserService = (): { data?: Agent[]; loading: boolean; error?: ApolloError } => {
  const {loading, error, data} = useQuery(GET_AGENTS_BY_USER_SERVICE, {
    // fetchPolicy: 'cache-only'
  })

  return {loading, error, data: data?.agentsByUserService}
}

export default useAgentsByUserService
