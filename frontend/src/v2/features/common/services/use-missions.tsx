import { useInfiniteQuery } from '@tanstack/react-query'
import { DYNAMIC_DATA_STALE_TIME } from '../../../../query-client'
import axios from '../../../../query-client/axios.ts'
import { MissionListItemDTO, MissionListPageDTO } from '../types/mission-types.ts'
import { missionsKeys } from './query-keys.ts'
import { useOnlineManager } from '../hooks/use-online-manager.tsx'

export const MISSION_LIST_PAGE_SIZE = 15

export type UseMissionsResult = {
  missions: MissionListItemDTO[]
  isLoading: boolean
  isError: boolean
  hasNextPage: boolean
  isFetchingNextPage: boolean
  fetchNextPage: () => void
}

/**
 * Paginated ("load more") mission list. The list endpoint returns a `{ items, hasMore, nextOffset }` page;
 * we drive an infinite query keyed on the filters/dates (paging params excluded) so changing a filter
 * restarts paging, and expose the flattened missions plus `fetchNextPage` / `hasNextPage`.
 *
 * `params` carries the optional date range + status/completeness/report-type filters (no `offset`/`limit`).
 */
const useMissionsQuery = (params: URLSearchParams): UseMissionsResult => {
  const { isOnline } = useOnlineManager()

  // Key on the filters/dates only (never the paging cursor), so a filter change starts a fresh paginate
  // while "load more" keeps appending to the same cache entry.
  const filterKey = new URLSearchParams(params)
  filterKey.delete('offset')
  filterKey.delete('limit')

  const query = useInfiniteQuery<MissionListPageDTO, Error>({
    queryKey: missionsKeys.filter(filterKey.toString()),
    initialPageParam: 0,
    queryFn: async ({ pageParam }) => {
      const requestParams = new URLSearchParams(params)
      requestParams.set('offset', String(pageParam))
      requestParams.set('limit', String(MISSION_LIST_PAGE_SIZE))
      const response = await axios.get<MissionListPageDTO>(`missions?${requestParams.toString()}`)
      return response.data
    },
    getNextPageParam: lastPage => (lastPage.hasMore ? lastPage.nextOffset : undefined),
    enabled: isOnline, // dates are optional now — no start/end required
    staleTime: DYNAMIC_DATA_STALE_TIME, // Cache data for 3 minutes
    retry: 2,
    refetchInterval: DYNAMIC_DATA_STALE_TIME
  })

  // Filters are applied server-side inside the window loop, so each page comes back already filled with up to
  // `limit` matches (or `hasMore = false` once matches are exhausted) — no empty-but-more-remain pages to skip.
  const missions = query.data?.pages.flatMap(page => page.items) ?? []

  return {
    missions,
    isLoading: query.isLoading,
    isError: query.isError,
    hasNextPage: query.hasNextPage,
    isFetchingNextPage: query.isFetchingNextPage,
    fetchNextPage: query.fetchNextPage
  }
}

export default useMissionsQuery
