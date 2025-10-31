import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { STATIC_DATA_GC_TIME, STATIC_DATA_STALE_TIME } from '../../../../query-client/index.ts'
import { agentServicesKeys } from '../../common/services/query-keys.ts'
import { AdminServiceWithAgent } from '../types/admin-agent-types.ts'

const useGetAgentServices = () => {
  const fetchAgentServices = (): Promise<AdminServiceWithAgent[]> =>
    axios.get(`/admin/agent_services`).then(response => response.data)

  const query = useQuery<AdminServiceWithAgent[]>({
    queryKey: agentServicesKeys.all(),
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

export default useGetAgentServices
