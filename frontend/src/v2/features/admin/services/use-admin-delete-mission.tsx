import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'

const useAdminDeleteMissionMutation = (): UseMutationResult<void, Error, string | undefined, unknown> => {
  const queryClient = useQueryClient()

  const deleteMission = (id?: string): Promise<void> => {
    return axios.delete(`admin/missions/${id}`)
  }

  return useMutation({
    mutationFn: deleteMission,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['admin-missions'] }),
    onError: error => {
      Sentry.captureException(error)
      queryClient.invalidateQueries({ queryKey: ['admin-missions'] })
    },
    scope: {
      id: `delete-admin-mission`
    }
  })
}

export default useAdminDeleteMissionMutation
