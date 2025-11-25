import * as Sentry from '@sentry/react'
import { useMutation } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import queryClient from '../../../../query-client/index.ts'

const useAdminAgentDisableMutation = () => {
  const disabled = (agentId: number): Promise<void> =>
    axios.post(`admin/agents/${agentId}/disable`).then(response => response.data)

  return useMutation({
    mutationFn: disabled,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['admin-agents'] }),
    onError: error => {
      Sentry.captureException(error)
      queryClient.invalidateQueries({ queryKey: ['admin-agents'] })
    },
    scope: {
      id: `create-admin-agent`
    }
  })
}

export default useAdminAgentDisableMutation
