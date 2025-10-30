import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'
import { agentsKeys } from '../../common/services/query-keys.ts'
import { Agent } from '../types/admin-agent-types.ts'

const useAdminCreateOrUpdateAgentMutation = (): UseMutationResult<Agent, Error, Agent, unknown> => {
  const queryClient = useQueryClient()

  const createOrUpdateAgent = (agent: Agent): Promise<Agent> => {
    const url = `admin/agents`
    if (agent.id) {
      return axios.put(url, agent).then(response => response.data)
    }
    return axios.post(url, agent).then(response => response.data)
  }

  return useMutation({
    mutationFn: createOrUpdateAgent,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: agentsKeys.all() }),
    onError: error => {
      Sentry.captureException(error)
      queryClient.invalidateQueries({ queryKey: agentsKeys.all() })
    },
    scope: {
      id: `create-admin-agent`
    }
  })
}

export default useAdminCreateOrUpdateAgentMutation
