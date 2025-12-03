import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'
import { ApiKey } from '../types/admin-apikey-types.ts'

const useAdminRotateApiKeyMutation = (): UseMutationResult<ApiKey, Error, ApiKey, unknown> => {
  const queryClient = useQueryClient()

  const rotateApiKey = (key: ApiKey): Promise<ApiKey> => {
    const url = `admin/apikey/${key.publicId}/rotate`
    return axios.post(url, key).then(response => response.data)
  }

  return useMutation({
    mutationFn: rotateApiKey,
    onSettled: async () => {
      await queryClient.invalidateQueries({ queryKey: ['admin-apikeys'] })
    },
    onError: error => {
      Sentry.captureException(error)
    },
    scope: {
      id: `rotate-admin-apikey`
    }
  })
}

export default useAdminRotateApiKeyMutation
