import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { STATIC_DATA_GC_TIME, STATIC_DATA_STALE_TIME } from '../../../../query-client/index.ts'
import { Agent } from '../types/admin-agent-types.ts'

const useGetAdminAgentServices = () => {
  const fetchAgentServices = (): Promise<Agent[]> => axios.get(`admin/agents`).then(response => response.data)

  const query = useQuery<Agent[]>({
    queryKey: ['admin-agents'],
    queryFn: fetchAgentServices,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2,
    gcTime: STATIC_DATA_GC_TIME,
    staleTime: STATIC_DATA_STALE_TIME
  })
  return query
}

export default useGetAdminAgentServices
