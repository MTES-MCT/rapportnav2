import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { STATIC_DATA_GC_TIME, STATIC_DATA_STALE_TIME } from '../../../../query-client/index.ts'
import { Vessel } from '../types/vessel-type.ts'
import { vesselsKeys } from './query-keys.ts'

const useVesselListQuery = () => {
  const fetchVessels = (): Promise<Vessel[]> => axios.get(`vessels`).then(response => response.data)

  const query = useQuery<Vessel[]>({
    queryKey: vesselsKeys.all(),
    queryFn: fetchVessels,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2,
    gcTime: STATIC_DATA_GC_TIME,
    staleTime: STATIC_DATA_STALE_TIME
  })
  return query
}

export default useVesselListQuery
