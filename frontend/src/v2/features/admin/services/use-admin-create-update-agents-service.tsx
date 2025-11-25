import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'
import { AdminAgent, AdminAgentInput } from '../types/admin-agent-types.ts'

const useAdminCreateOrUpdateAgentMutation = (): UseMutationResult<AdminAgent, Error, AdminAgentInput, unknown> => {
  const queryClient = useQueryClient()

  const createOrUpdateAgent = (agent: AdminAgentInput): Promise<AdminAgent> => {
    const url = `admin/agents`
    if (agent.id) {
      return axios.put(url, agent).then(response => response.data)
    }
    return axios.post(url, agent).then(response => response.data)
  }

  return useMutation({
    mutationFn: createOrUpdateAgent,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['admin-agents'] }),
    onError: error => {
      Sentry.captureException(error)
      queryClient.invalidateQueries({ queryKey: ['admin-agents'] })
    },
    scope: {
      id: `create-admin-agent`
    }
  })
}

export default useAdminCreateOrUpdateAgentMutation
