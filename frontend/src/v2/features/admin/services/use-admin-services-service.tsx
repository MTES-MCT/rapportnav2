import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { AdminService } from '../types/admin-services-type.ts'

interface UseAdminServiceListQueryOptions {
  active?: boolean
}

const useAdminServiceListQuery = (options?: UseAdminServiceListQueryOptions) => {
  const { active } = options ?? {}

  const fetchAdminServices = (): Promise<AdminService[]> => {
    const params = active !== undefined ? { active } : undefined
    return axios.get('admin/services', { params }).then(response => response.data)
  }

  const query = useQuery<AdminService[]>({
    queryKey: ['admin-services', { active }],
    queryFn: fetchAdminServices,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2
  })
  return query
}

export default useAdminServiceListQuery
