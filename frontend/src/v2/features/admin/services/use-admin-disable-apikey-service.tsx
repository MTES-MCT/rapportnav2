import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'
import { AdminUser } from '../types/admin-agent-types.ts'
import { ApiKey } from '../types/admin-apikey-types.ts'

const useAdminDisableApiKeyMutation = (): UseMutationResult<ApiKey, Error, ApiKey, unknown> => {
  const queryClient = useQueryClient()

  const disableApiKey = (key: ApiKey): Promise<ApiKey> => {
    const url = `admin/apikey/${key.publicId}/disable`
    return axios.patch(url, key).then(response => response.data)
  }

  return useMutation({
    mutationFn: disableApiKey,
    onSettled: async () => {
      await queryClient.invalidateQueries({ queryKey: ['admin-apikeys'] })
    },
    onError: error => {
      Sentry.captureException(error)
    },
    scope: {
      id: `disable-admin-apikey`
    }
  })
}

export default useAdminDisableApiKeyMutation
