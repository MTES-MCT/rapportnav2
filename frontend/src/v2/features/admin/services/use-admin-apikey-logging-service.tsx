import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { ApiKeyLogging } from '../types/admin-apikey-logging-types.ts'

const useApiKeyLoggingListQuery = () => {
  const fetchLogs = (): Promise<ApiKeyLogging[]> => axios.get(`admin/apikey/audit-logs`).then(response => response.data)

  const query = useQuery<ApiKeyLogging[]>({
    queryFn: fetchLogs,
    queryKey: ['admin-apikey-logging'],
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2,
    staleTime: 0
  })
  return query
}

export default useApiKeyLoggingListQuery
