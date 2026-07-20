import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { AdminMissionAction } from '../types/admin-mission-action-types.ts'
import { PaginatedResponse } from '../types/pagination-types.ts'

const useMissionActionsListQuery = (
  page: number = 0,
  pageSize: number = 10,
  searchId?: string,
  searchOwnerId?: string
) => {
  const fetchMissionActions = (): Promise<PaginatedResponse<AdminMissionAction>> =>
    axios
      .get(`admin/mission-actions`, {
        params: {
          page,
          size: pageSize,
          searchId: searchId || undefined,
          searchOwnerId: searchOwnerId || undefined
        }
      })
      .then(response => response.data)

  return useQuery<PaginatedResponse<AdminMissionAction>>({
    queryKey: ['admin-mission-actions', page, pageSize, searchId, searchOwnerId],
    queryFn: fetchMissionActions,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2,
    staleTime: 0,
    gcTime: 0
  })
}

export default useMissionActionsListQuery
