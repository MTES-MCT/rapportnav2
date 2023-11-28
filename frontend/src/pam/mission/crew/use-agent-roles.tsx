import { ApolloError, useQuery } from '@apollo/client'
import { GET_AGENT_ROLES } from '../queries'
import { AgentRole } from '../../../types/crew-types'

const useAgentRoles = (): { data?: AgentRole[]; loading: boolean; error?: ApolloError } => {
  const { loading, error, data } = useQuery(GET_AGENT_ROLES, {
    // fetchPolicy: 'cache-only'
  })

  return { loading, error, data: data?.agentRoles }
}

export default useAgentRoles
