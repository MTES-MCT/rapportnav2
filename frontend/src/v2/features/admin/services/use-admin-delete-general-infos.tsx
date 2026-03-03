import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'

const useAdminDeleteGeneralInfosMutation = (): UseMutationResult<void, Error, number | undefined, unknown> => {
  const queryClient = useQueryClient()

  const deleteGeneralInfos = (id?: number): Promise<void> => {
    return axios.delete(`admin/general-infos/${id}`)
  }

  return useMutation({
    mutationFn: deleteGeneralInfos,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['admin-general-infos'] }),
    onError: error => {
      Sentry.captureException(error)
      queryClient.invalidateQueries({ queryKey: ['admin-general-infos'] })
    },
    scope: {
      id: `delete-admin-general-infos`
    }
  })
}

export default useAdminDeleteGeneralInfosMutation
