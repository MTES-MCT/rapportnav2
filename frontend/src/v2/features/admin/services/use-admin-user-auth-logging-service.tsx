import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { UserAuthLogging } from '../types/admin-user-auth-logging-types.ts'

const useUserAuthLoggingListQuery = () => {
  const fetchLogs = (): Promise<UserAuthLogging[]> => axios.get(`admin/users/auth-logs`).then(response => response.data)

  const query = useQuery<UserAuthLogging[]>({
    queryFn: fetchLogs,
    queryKey: ['admin-user-auth-logging'],
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2,
    staleTime: 0
  })
  return query
}

export default useUserAuthLoggingListQuery
