import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'
import { ApiKey } from '../types/admin-apikey-types.ts'

const useAdminCreateOrUpdateApiKeyMutation = (): UseMutationResult<ApiKey, Error, ApiKey, unknown> => {
  const queryClient = useQueryClient()

  const createOrUpdateApiKey = (key: ApiKey): Promise<ApiKey> => {
    const url = `admin/apikey`
    if (key.id) {
      return axios.put(url, key).then(response => response.data)
    }
    return axios.post(url, key).then(response => response.data)
  }

  return useMutation({
    mutationFn: createOrUpdateApiKey,
    onSettled: async () => {
      await queryClient.invalidateQueries({ queryKey: ['admin-apikeys'] })
    },
    onError: error => {
      Sentry.captureException(error)
    },
    scope: {
      id: `create-admin-apikey`
    }
  })
}

export default useAdminCreateOrUpdateApiKeyMutation
