import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'

const useManageDeleteAgentMutation = (serviceId?: number): UseMutationResult<void, Error, number, unknown> => {
  const queryClient = useQueryClient()

  const deleteAgent = (id: number): Promise<void> => {
    return axios.delete(`manage/services/${serviceId}/agents/${id}`)
  }

  return useMutation({
    mutationFn: deleteAgent,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['manage-agents'] }),
    onError: error => {
      Sentry.captureException(error)
      queryClient.invalidateQueries({ queryKey: ['manage-agents'] })
    },
    scope: {
      id: `delete-manage-agent`
    }
  })
}

export default useManageDeleteAgentMutation
