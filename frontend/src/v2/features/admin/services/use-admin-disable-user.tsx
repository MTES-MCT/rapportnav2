import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'
import { AdminUser } from '../types/admin-agent-types.ts'

const useAdminDisableUserMutation = (): UseMutationResult<AdminUser | null, Error, number | undefined, unknown> => {
  const queryClient = useQueryClient()

  const disableUser = (id?: number): Promise<AdminUser | null> => {
    return axios.post(`admin/users/${id}/disable`).then(response => response.data)
  }

  return useMutation({
    mutationFn: disableUser,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['admin-users'] }),
    onError: error => {
      Sentry.captureException(error)
      queryClient.invalidateQueries({ queryKey: ['admin-users'] })
    },
    scope: {
      id: `disable-admin-user`
    }
  })
}

export default useAdminDisableUserMutation