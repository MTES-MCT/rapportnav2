import {  useQuery } from '@tanstack/react-query'

import axios from '../../../../query-client/axios.ts'
import { Agent } from '../types/crew-type.ts'

const useAgentsQuery = () => {
  const fetchAgents = (): Promise<Agent[]> => axios.get(`agents`).then(response => response.data)

  const query = useQuery<Agent[], Error>({
    queryKey: ['agents'],
    queryFn: fetchAgents,
    retry: 2 // Retry failed requests twice before throwing an error
  })
  return query
}

export default useAgentsQuery
