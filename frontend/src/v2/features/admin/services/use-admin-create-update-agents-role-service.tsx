import { useMutation, UseMutationResult, useQueryClient } from '@tanstack/react-query'

import * as Sentry from '@sentry/react'
import axios from '../../../../query-client/axios.ts'
import { agentRolesKeys } from '../../common/services/query-keys.ts'
import { AgentRole } from '../types/admin-agent-types.ts'

const useAdminCreateOrUpdateAgentRoleMutation = (): UseMutationResult<AgentRole, Error, AgentRole, unknown> => {
  const queryClient = useQueryClient()

  const createOrUpdateAgentRole = (agent: AgentRole): Promise<AgentRole> => {
    const url = `admin/agent_roles`
    if (agent.id) {
      return axios.put(url, agent).then(response => response.data)
    }
    return axios.post(url, agent).then(response => response.data)
  }

  return useMutation({
    mutationFn: createOrUpdateAgentRole,
    onSuccess: () => queryClient.invalidateQueries({ queryKey: agentRolesKeys.all() }),
    onError: error => {
      Sentry.captureException(error)
      queryClient.invalidateQueries({ queryKey: agentRolesKeys.all() })
    },
    scope: {
      id: `create-admin-agent-role`
    }
  })
}

export default useAdminCreateOrUpdateAgentRoleMutation
