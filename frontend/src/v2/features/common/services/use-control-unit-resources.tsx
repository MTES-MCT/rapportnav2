import { useQuery } from '@tanstack/react-query'

import axios from '../../../../query-client/axios.ts'
import { ControlUnitResource } from '../types/control-unit-types.ts'
import { controlUnitResourcesKeys } from './query-keys.ts'

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
    retry: 2 // Retry failed requests twice before throwing an error
  })

  return query
}

export default useControlUnitResourcesQuery
