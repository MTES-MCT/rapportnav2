import { useQuery } from '@tanstack/react-query'

import axios from '../../../../query-client/axios.ts'
import { ControlUnitResource } from '../types/control-unit-types.ts'

const useControlUnitResourcesQuery = (controlUnitId?: number) => {
  const fetchControlUnitResources = (): Promise<ControlUnitResource[]> => {
    return axios
      .get(`resources`)
      .then(response => response.data)
      .then((value: ControlUnitResource[]) =>
        controlUnitId ? value.filter(c => c.controlUnitId === controlUnitId) : value
      )
  }

  const query = useQuery<ControlUnitResource[], Error>({
    queryKey: ['controlUnitResources'],
    queryFn: fetchControlUnitResources,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    retry: 2 // Retry failed requests twice before throwing an error
  })

  return query
}

export default useControlUnitResourcesQuery
