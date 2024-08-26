import { ApolloError, gql, useQuery } from '@apollo/client'
import { AgentRole } from '../../../common/types/crew-types.ts'

export const GET_AGENT_ROLES = gql`
  query GetAgentRoles {
    agentRoles {
      id
      title
    }
  }
`

const useAgentRoles = (): { data?: AgentRole[]; loading: boolean; error?: ApolloError } => {
  const { loading, error, data } = useQuery(GET_AGENT_ROLES, {
    // fetchPolicy: 'cache-only'
  })

  return { loading, error, data: data?.agentRoles }
}

export default useAgentRoles
