import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'
import { AdminUser } from '../types/admin-agent-types.ts'

const useAdminCreateOrUpdateUserMutation = (): UseMutationResult<AdminUser, Error, AdminUser, unknown> => {
  const queryClient = useQueryClient()

  const createOrUpdateUser = (user: AdminUser): Promise<AdminUser> => {
    const url = `admin/users`
    if (user.id) {
      return axios.put(url, user).then(response => response.data)
    }
    return axios.post(url, user).then(response => response.data)
  }

  return useMutation({
    mutationFn: createOrUpdateUser,
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

export default useAdminCreateOrUpdateUserMutation
