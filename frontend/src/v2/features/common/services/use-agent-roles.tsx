import axios from '../../../../query-client/axios.ts'
import { useQuery } from '@tanstack/react-query'
import { AgentRole } from '@common/types/crew-types.ts'

const useGetAgentRoles = () => {
  const fetchAgentRoles = (): Promise<AgentRole[]> => axios.get(`agent_roles`).then(response => response.data)

  const query = useQuery<AgentRole[]>({
    queryKey: ['agentRoles'],
    queryFn: fetchAgentRoles
  })
  return query
}

export default useGetAgentRoles
