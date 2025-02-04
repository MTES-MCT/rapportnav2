import {  useQuery } from '@tanstack/react-query'

import axios from '../../../../query-client/axios.ts'
import { AgentRole } from '../../common/types/crew-type.ts'

const useAgentRolesQuery = () => {
  const fetchAgentRoles = (): Promise<AgentRole[]> => axios.get(`agent_roles`).then(response => response.data)

  const query =  useQuery<AgentRole[], Error>({
    queryKey: ['agentRoles'],
    queryFn: fetchAgentRoles,
    retry: 2 // Retry failed requests twice before throwing an error
  })

  return query

}

export default useAgentRolesQuery()
