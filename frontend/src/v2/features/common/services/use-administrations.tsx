import { useQuery } from '@tanstack/react-query'

import axios from '../../../../query-client/axios.ts'
import { Administration } from '../types/control-unit-types.ts'
import { administrationKeys } from './query-keys.ts'

const useAdministrationsQuery = () => {
  const fetchAdministrations = (): Promise<Administration[]> =>
    axios.get(`administrations`).then(response => response.data)

  const query = useQuery<Administration[], Error>({
    queryKey: administrationKeys.all(),
    queryFn: fetchAdministrations,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2
  })

  return query
}

export default useAdministrationsQuery
