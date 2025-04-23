import axios from '../../../../query-client/axios.ts'
import { useQuery } from '@tanstack/react-query'
import { AgentRole } from '@common/types/crew-types.ts'
import { agentRolesKeys } from './query-keys.ts'
import { STATIC_DATA_GC_TIME, STATIC_DATA_STALE_TIME } from '../../../../query-client'

const useGetAgentRoles = () => {
  const fetchAgentRoles = (): Promise<AgentRole[]> => axios.get(`agent_roles`).then(response => response.data)

  const query = useQuery<AgentRole[]>({
    queryKey: agentRolesKeys.all(),
    queryFn: fetchAgentRoles,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2,
    gcTime: STATIC_DATA_GC_TIME,
    staleTime: STATIC_DATA_STALE_TIME
  })
  return query
}

export default useGetAgentRoles
