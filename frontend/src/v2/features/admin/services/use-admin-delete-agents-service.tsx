import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'

const useAdminDeleteAgentMutation = (): UseMutationResult<void, Error, number | undefined, unknown> => {
  const queryClient = useQueryClient()

  const deleteAgent = (id?: number): Promise<void> => {
    return axios.delete(`admin/agents/${id}`)
  }

  return useMutation({
    mutationFn: deleteAgent,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['admin-agents'] }),
    onError: error => {
      Sentry.captureException(error)
      queryClient.invalidateQueries({ queryKey: ['admin-agents'] })
    },
    scope: {
      id: `delete-admin-agent`
    }
  })
}

export default useAdminDeleteAgentMutation
