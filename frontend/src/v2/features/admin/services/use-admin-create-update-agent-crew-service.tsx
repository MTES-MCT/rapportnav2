import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'
import { AdminAgentServiceInput, AdminServiceWithAgent } from '../types/admin-agent-types.ts'

const useAdminCreateOrUpdateAgentCrewMutation = (): UseMutationResult<
  AdminServiceWithAgent,
  Error,
  AdminAgentServiceInput,
  unknown
> => {
  const queryClient = useQueryClient()

  const createOrUpdateCrew = (service: AdminAgentServiceInput): Promise<AdminServiceWithAgent> => {
    const url = `admin/agent_services`
    if (service.id) {
      return axios.put(url, service).then(response => response.data)
    }
    return axios.post(url, service).then(response => response.data)
  }

  return useMutation({
    mutationFn: createOrUpdateCrew,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['admin-agent-services'] }),
    onError: error => {
      Sentry.captureException(error)
      queryClient.invalidateQueries({ queryKey: ['admin-agent-services'] })
    },
    scope: {
      id: `create-admin-crew`
    }
  })
}

export default useAdminCreateOrUpdateAgentCrewMutation
