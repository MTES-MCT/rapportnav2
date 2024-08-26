import { ApolloError, gql, useQuery } from '@apollo/client'
import { Agent } from '../../../common/types/crew-types.ts'

export const GET_AGENTS = gql`
  query GetAgents {
    agents {
      id
      firstName
      lastName
    }
  }
`

const useAgents = (): { data?: Agent[]; loading: boolean; error?: ApolloError } => {
  const { loading, error, data } = useQuery(GET_AGENTS, {
    // fetchPolicy: 'cache-only'
  })

  return { loading, error, data: data?.agents }
}

export default useAgents
