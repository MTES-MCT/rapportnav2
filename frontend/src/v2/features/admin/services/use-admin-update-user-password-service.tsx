import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'

const useAdminUserPasswordMutation = (): UseMutationResult<
  void,
  Error,
  { userId: number; password: string },
  unknown
> => {
  const queryClient = useQueryClient()

  const updateUserPassword = ({ userId, password }: { userId: number; password: string }): Promise<void> => {
    return axios.post(`admin/users/password/${userId}`, { password }).then(response => response.data)
  }

  return useMutation({
    mutationFn: updateUserPassword,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['admin-users'] }),
    onError: error => {
      Sentry.captureException(error)
      queryClient.invalidateQueries({ queryKey: ['admin-users'] })
    },
    scope: {
      id: `create-admin-users`
    }
  })
}

export default useAdminUserPasswordMutation
