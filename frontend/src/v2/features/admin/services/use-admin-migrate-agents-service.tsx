import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'
import { AdminAgent, AdminAgentInput } from '../types/admin-agent-types.ts'

const useAdminMigrateAgentMutation = (): UseMutationResult<AdminAgent, Error, AdminAgentInput, unknown> => {
  const queryClient = useQueryClient()

  const migrateAgent = (agent: AdminAgentInput): Promise<AdminAgent> => {
    return axios.post(`admin/agents/${agent.id}/migrate`, agent).then(response => response.data)
  }

  return useMutation({
    mutationFn: migrateAgent,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['admin-agents'] }),
    onError: error => {
      Sentry.captureException(error)
      queryClient.invalidateQueries({ queryKey: ['admin-agents'] })
    },
    scope: {
      id: `migrate-admin-agent`
    }
  })
}

export default useAdminMigrateAgentMutation
