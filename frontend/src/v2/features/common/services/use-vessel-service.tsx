import { skipToken, useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { Vessel } from '../types/vessel-type.ts'

export const useVesselListQuery = (search?: string) => {
  const fetchVessels = (): Promise<Vessel[]> => axios.get(`vessels?search=${search}`).then(response => response.data)

  const query = useQuery<Vessel[]>({
    queryKey: ['vessels', search],
    queryFn: search && search.length >= 3 ? () => fetchVessels() : skipToken,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2
  })
  return query
}
