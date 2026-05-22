import { skipToken, useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { STATIC_DATA_GC_TIME, STATIC_DATA_STALE_TIME } from '../../../../query-client/index.ts'
import { AdminAgent } from '../../admin/types/admin-agent-types.ts'

const useGetManageAgentService = (serviceId?: number) => {
  const fetchAgentServices = (): Promise<AdminAgent[]> =>
    axios.get(`manage/services/${serviceId}/agents`).then(response => response.data)

  const query = useQuery<AdminAgent[]>({
    queryKey: ['manage-agents'],
    queryFn: serviceId ? () => fetchAgentServices() : skipToken,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2,
    gcTime: STATIC_DATA_GC_TIME,
    staleTime: STATIC_DATA_STALE_TIME
  })
  return query
}

export default useGetManageAgentService
