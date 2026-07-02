import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { Country } from '../types/country-type.ts'

const useCountryListQuery = () => {
  const fetchCountries = (): Promise<Country[]> => axios.get(`countries`).then(response => response.data)

  const query = useQuery<Country[]>({
    queryKey: ['countries'],
    queryFn: fetchCountries,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2
  })
  return query
}

export default useCountryListQuery
