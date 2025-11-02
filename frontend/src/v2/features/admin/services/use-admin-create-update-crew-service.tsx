import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'
import { agentServicesKeys } from '../../common/services/query-keys.ts'
import { AdminAgentServiceInput, AdminServiceWithAgent } from '../types/admin-agent-types.ts'

const useAdminCreateOrUpdateAgentServiceMutation = (): UseMutationResult<
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
    onSuccess: () => queryClient.invalidateQueries({ queryKey: agentServicesKeys.all() }),
    onError: error => {
      Sentry.captureException(error)
      queryClient.invalidateQueries({ queryKey: agentServicesKeys.all() })
    },
    scope: {
      id: `create-admin-crew`
    }
  })
}

export default useAdminCreateOrUpdateAgentServiceMutation
