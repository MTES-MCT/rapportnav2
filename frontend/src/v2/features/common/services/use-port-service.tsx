import { skipToken, useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { Port } from '../types/port-type.ts'

export const usePortListQuery = (search?: string) => {
  const fetchPorts = (): Promise<Port[]> => axios.get(`ports?search=${search}`).then(response => response.data)

  const query = useQuery<Port[]>({
    queryKey: ['ports', search],
    queryFn: search && search.length >= 3 ? () => fetchPorts() : skipToken,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2
  })
  return query
}

export const usePortListAllQuery = () => {
  const fetchAllPorts = (): Promise<Port[]> => axios.get('ports').then(response => response.data)

  return useQuery<Port[]>({
    queryKey: ['ports-all'],
    queryFn: fetchAllPorts,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2
  })
}
