import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'

const useAdminDeleteServiceMutation = (): UseMutationResult<void, Error, number, unknown> => {
  const queryClient = useQueryClient()

  const deleteService = (id: number): Promise<void> => {
    return axios.delete(`admin/services/${id}`)
  }

  return useMutation({
    mutationFn: deleteService,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['admin-services'] }),
    onError: error => {
      Sentry.captureException(error)
      queryClient.invalidateQueries({ queryKey: ['admin-services'] })
    },
    scope: {
      id: `delete-admin-services`
    }
  })
}

export default useAdminDeleteServiceMutation
