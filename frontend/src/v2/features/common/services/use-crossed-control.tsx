import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { CrossControl } from '../types/crossed-control-type.ts'
import { crossControlsKeys } from './query-keys.ts'

const useCrossControlListQuery = () => {
  const fetchVessels = (): Promise<CrossControl[]> => axios.get(`cross_controls`).then(response => response.data)

  const query = useQuery<CrossControl[]>({
    queryKey: crossControlsKeys.all(),
    queryFn: fetchVessels,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2
  })
  return query
}

export default useCrossControlListQuery
