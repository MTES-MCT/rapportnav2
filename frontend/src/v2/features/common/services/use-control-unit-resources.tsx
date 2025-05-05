import { useQuery } from '@tanstack/react-query'

import axios from '../../../../query-client/axios.ts'
import { ControlUnitResource } from '../types/control-unit-types.ts'
import { controlUnitResourcesKeys } from './query-keys.ts'
import { STATIC_DATA_GC_TIME, STATIC_DATA_STALE_TIME } from '../../../../query-client'

const useControlUnitResourcesQuery = (controlUnitId?: number) => {
  const fetchControlUnitResources = async (): Promise<ControlUnitResource[]> => {
    const response = await axios.get(`resources`)
    const value = response.data
    return value.filter((c: ControlUnitResource) => c.controlUnitId === controlUnitId) ?? []
  }

  const query = useQuery<ControlUnitResource[], Error>({
    queryKey: controlUnitResourcesKeys.all(),
    queryFn: fetchControlUnitResources,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2, // Retry failed requests twice before throwing an error
    gcTime: STATIC_DATA_GC_TIME,
    staleTime: STATIC_DATA_STALE_TIME
  })

  return query
}

export default useControlUnitResourcesQuery
