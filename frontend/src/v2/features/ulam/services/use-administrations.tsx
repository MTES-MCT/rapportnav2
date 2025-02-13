import {  useQuery } from '@tanstack/react-query'

import axios from '../../../../query-client/axios.ts'
import { Administration } from '../../common/types/control-unit-types.ts'

const useAdministrationsQuery = () => {
  const fetchAdministrations = (): Promise<Administration[]> => axios.get(`administrations`).then(response => response.data)

  const query =  useQuery<Administration[], Error>({
    queryKey: ['administrations'],
    queryFn: fetchAdministrations,
    retry: 2 // Retry failed requests twice before throwing an error
  })

  return query

}

export default useAdministrationsQuery
