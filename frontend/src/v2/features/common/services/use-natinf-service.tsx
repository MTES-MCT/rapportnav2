import { Natinf } from '@common/types/infraction-types'
import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { natinfsKeys } from './query-keys.ts'
import { STATIC_DATA_GC_TIME, STATIC_DATA_STALE_TIME } from '../../../../query-client'

const useNatinfListQuery = () => {
  const fetchNatInfs = (): Promise<Natinf[]> => axios.get(`natinfs`).then(response => response.data)

  const query = useQuery<Natinf[]>({
    queryKey: natinfsKeys.all(),
    queryFn: fetchNatInfs,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2, // Retry failed requests twice before throwing an error
    gcTime: STATIC_DATA_GC_TIME,
    staleTime: STATIC_DATA_STALE_TIME
  })
  return query
}

export default useNatinfListQuery
