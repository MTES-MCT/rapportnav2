import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { STATIC_DATA_GC_TIME, STATIC_DATA_STALE_TIME } from '../../../../query-client/index.ts'
import { ServiceWithAgents } from '../types/service-agents-types.ts'
import { agentServicesKeys } from './query-keys.ts'

const useGetAgentServices = () => {
  const fetchAgentServices = (): Promise<ServiceWithAgents[]> => axios.get(`crews`).then(response => response.data)

  const query = useQuery<ServiceWithAgents[]>({
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
