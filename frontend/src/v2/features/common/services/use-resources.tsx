import { useQuery } from '@tanstack/react-query'

import { STATIC_DATA_GC_TIME, STATIC_DATA_STALE_TIME } from '../../../..//query-client/index.ts'
import axios from '../../../../query-client/axios.ts'
import { ControlUnitResource } from '../types/control-unit-types.ts'
import { resourcesKeys } from './query-keys.ts'

const useResourcesQuery = () => {
  const fetchResources = async (): Promise<ControlUnitResource[]> => {
    return (await axios.get(`resources`)).data
  }

  const query = useQuery<ControlUnitResource[], Error>({
    queryKey: resourcesKeys.all(),
    queryFn: fetchResources,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2,
    gcTime: STATIC_DATA_GC_TIME,
    staleTime: STATIC_DATA_STALE_TIME
  })

  return query
}

export default useResourcesQuery
