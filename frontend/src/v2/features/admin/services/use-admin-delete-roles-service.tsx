import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'
import { agentRolesKeys } from '../../common/services/query-keys.ts'

const useAdminDeleteAgentRoleMutation = (): UseMutationResult<void, Error, number | undefined, unknown> => {
  const queryClient = useQueryClient()

  const deleteAgentRole = (id?: number): Promise<void> => {
    return axios.delete(`admin/agent_roles/${id}`)
  }

  return useMutation({
    mutationFn: deleteAgentRole,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: [agentRolesKeys.all()] }),
    onError: error => {
      Sentry.captureException(error)
      queryClient.invalidateQueries({ queryKey: [agentRolesKeys.all()] })
    },
    scope: {
      id: `delete-admin-agent-roles`
    }
  })
}

export default useAdminDeleteAgentRoleMutation
