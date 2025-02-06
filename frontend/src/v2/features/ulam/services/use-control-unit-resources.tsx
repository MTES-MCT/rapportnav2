import {  useQuery } from '@tanstack/react-query'

import axios from '../../../../query-client/axios.ts'
import { ControlUnitResource } from '../../common/types/control-unit-types.ts'

const useControlUnitResourcesQuery = () => {
  const fetchControlUnitResources = (): Promise<ControlUnitResource[]> => axios.get(`resources`).then(response => response.data)

  const query =  useQuery<ControlUnitResource[], Error>({
    queryKey: ['controlUnitResources'],
    queryFn: fetchControlUnitResources,
    retry: 2 // Retry failed requests twice before throwing an error
  })

  return query

}

export default useControlUnitResourcesQuery
