import { ApolloError, useQuery } from '@apollo/client'
import { GET_AGENTS_BY_SERVICE } from '../queries'
import { Agent } from '../../../types/crew-types'

const useAgentsByService = (serviceId: string): { data?: Agent[]; loading: boolean; error?: ApolloError } => {
  const { loading, error, data } = useQuery(GET_AGENTS_BY_SERVICE, {
    variables: { serviceId }
    // fetchPolicy: 'cache-only'
  })

  return { loading, error, data: data?.agentsByServiceId }
}

export default useAgentsByService
