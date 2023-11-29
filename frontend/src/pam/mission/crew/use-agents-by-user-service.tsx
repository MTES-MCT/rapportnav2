import { ApolloError, useQuery } from '@apollo/client'
import { GET_AGENTS_BY_USER_SERVICE } from '../queries'
import { Agent } from '../../../types/crew-types'

const useAgentsByUserService = (): { data?: Agent[]; loading: boolean; error?: ApolloError } => {
  const { loading, error, data } = useQuery(GET_AGENTS_BY_USER_SERVICE, {
    // fetchPolicy: 'cache-only'
  })

  return { loading, error, data: data?.agentsByServiceId }
}

export default useAgentsByUserService
