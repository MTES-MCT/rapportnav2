import { useQuery } from '@tanstack/react-query'

import axios from '../../../../query-client/axios.ts'
import { Agent } from '../types/crew-type.ts'
import { agentsKeys } from './query-keys.ts'
import { STATIC_DATA_GC_TIME, STATIC_DATA_STALE_TIME } from '../../../../query-client'

const useAgentsQuery = () => {
  const fetchAgents = (): Promise<Agent[]> => axios.get(`agents`).then(response => response.data)

  const query = useQuery<Agent[], Error>({
    queryKey: agentsKeys.all(),
    queryFn: fetchAgents,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2, // Retry failed requests twice before throwing an error
    gcTime: STATIC_DATA_GC_TIME,
    staleTime: STATIC_DATA_STALE_TIME
  })
  return query
}

export default useAgentsQuery
