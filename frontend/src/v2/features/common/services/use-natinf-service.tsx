import { Natinf } from '@common/types/infraction-types'
import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { natinfsKeys } from './query-keys.ts'

const useNatinfListQuery = () => {
  const fetchNatInfs = (): Promise<Natinf[]> => axios.get(`natinfs`).then(response => response.data)

  const query = useQuery<Natinf[]>({
    queryKey: natinfsKeys.all(),
    queryFn: fetchNatInfs,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2 // Retry failed requests twice before throwing an error
  })
  return query
}

export default useNatinfListQuery
