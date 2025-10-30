import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { AdminService } from '../types/admin-services-type.ts'

const useAdminServiceListQuery = () => {
  const fetchAdminServices = (): Promise<AdminService[]> => axios.get(`admin/services`).then(response => response.data)

  const query = useQuery<AdminService[]>({
    queryKey: ['admin-services'],
    queryFn: fetchAdminServices,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2
  })
  return query
}

export default useAdminServiceListQuery
