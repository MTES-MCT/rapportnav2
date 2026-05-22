import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'
import { AdminAgent, AdminAgentInput } from '../../admin/types/admin-agent-types.ts'

const useManageUpsertAgentMutation = (
  serviceId?: number
): UseMutationResult<AdminAgent, Error, AdminAgentInput, unknown> => {
  const queryClient = useQueryClient()

  const upsertAgent = (agent: AdminAgentInput): Promise<AdminAgent> => {
    const url = `manage/services/${serviceId}/agents`
    if (agent.id) {
      return axios.put(url, agent).then(response => response.data)
    }
    return axios.post(url, agent).then(response => response.data)
  }

  return useMutation({
    mutationFn: upsertAgent,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['manage-agents'] }),
    onError: error => {
      Sentry.captureException(error)
      queryClient.invalidateQueries({ queryKey: ['manage-agents'] })
    },
    scope: {
      id: `upsert-manage-agent`
    }
  })
}

export default useManageUpsertAgentMutation
