import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'
import { AdminService } from '../types/admin-services-type.ts'

const useAdminCreateOrUpdateServiceMutation = (): UseMutationResult<AdminService, Error, AdminService, unknown> => {
  const queryClient = useQueryClient()

  const createOrUpdateService = (service: AdminService): Promise<AdminService> => {
    const url = `admin/services`
    if (service.id) {
      return axios.put(url, service).then(response => response.data)
    }
    return axios.post(url, service).then(response => response.data)
  }

  return useMutation({
    mutationFn: createOrUpdateService,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['admin-services'] }),
    onError: error => {
      Sentry.captureException(error)
      queryClient.invalidateQueries({ queryKey: ['admin-services'] })
    },
    scope: {
      id: `create-admin-service`
    }
  })
}

export default useAdminCreateOrUpdateServiceMutation
