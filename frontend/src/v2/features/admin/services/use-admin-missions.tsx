import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { AdminMission } from '../types/admin-mission-types.ts'
import { PaginatedResponse } from '../types/pagination-types.ts'

const useMissionsListQuery = (page: number = 0, pageSize: number = 10, searchId?: string) => {
  const fetchMissions = (): Promise<PaginatedResponse<AdminMission>> =>
    axios
      .get(`admin/missions`, { params: { page, size: pageSize, searchId: searchId || undefined } })
      .then(response => response.data)

  return useQuery<PaginatedResponse<AdminMission>>({
    queryKey: ['admin-missions', page, pageSize, searchId],
    queryFn: fetchMissions,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2,
    staleTime: 0,
    gcTime: 0
  })
}

export default useMissionsListQuery
