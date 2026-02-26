import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { AdminGeneralInfos } from '../types/admin-general-infos-types.ts'
import { PaginatedResponse } from '../types/pagination-types.ts'

const useGeneralInfosListQuery = (page: number = 0, pageSize: number = 10, search?: string) => {
  const fetchGeneralInfos = (): Promise<PaginatedResponse<AdminGeneralInfos>> =>
    axios
      .get(`admin/general-infos`, { params: { page, size: pageSize, search: search || undefined } })
      .then(response => response.data)

  const query = useQuery<PaginatedResponse<AdminGeneralInfos>>({
    queryKey: ['admin-general-infos', page, pageSize, search],
    queryFn: fetchGeneralInfos,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2,
    staleTime: 0,
    gcTime: 0
  })
  return query
}

export default useGeneralInfosListQuery
