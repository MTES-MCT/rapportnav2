import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { ApiKey } from '../types/admin-apikey-types.ts'

const useApiKeyListQuery = () => {
  const fetchApiKeys = (): Promise<ApiKey[]> => axios.get(`admin/apikey`).then(response => response.data)

  const query = useQuery<ApiKey[]>({
    queryFn: fetchApiKeys,
    queryKey: ['admin-apikeys'],
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2,
    staleTime: 0
  })
  return query
}

export default useApiKeyListQuery
