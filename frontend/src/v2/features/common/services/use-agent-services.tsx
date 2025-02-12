import axios from '../../../../query-client/axios.ts'
import { useQuery } from '@tanstack/react-query'
import { ServiceWithAgents } from '../types/service-agents-types.ts'

const useGetAgentServices = () => {
  const fetchAgentServices = (): Promise<ServiceWithAgents[]> => axios.get(`crews`).then(response => response.data)

  const query = useQuery<ServiceWithAgents[]>({
    queryKey: ['agentServices'],
    queryFn: fetchAgentServices
  })
  return query
}

export default useGetAgentServices
