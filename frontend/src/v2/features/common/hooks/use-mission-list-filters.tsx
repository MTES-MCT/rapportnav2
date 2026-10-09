import { useQueryClient } from '@tanstack/react-query'
import { type SetURLSearchParams, useSearchParams } from 'react-router'
import {
  clearMissionListFilters,
  hasActiveMissionListFilters
} from '../components/elements/mission-list-filter-utils.ts'
import { missionsKeys } from '../services/query-keys.ts'

interface MissionListFiltersHook {
  searchParams: URLSearchParams
  setSearchParams: SetURLSearchParams
  // whether any mission-list filter is currently active
  filtersActive: boolean
  // clear every filter and refresh the list
  resetFilters: () => void
}

/**
 * Owns the URL-driven mission-list filter state shared by the ULAM and PAM list pages: exposes the
 * current search params, an `filtersActive` flag and a `resetFilters` action.
 *
 * `resetFilters` clears the filter params AND invalidates the missions cache: the offline-first React
 * Query config (`refetchOnMount: false`, long `gcTime`, persisted cache) otherwise serves the default
 * key from cache without refetching, so the list would keep showing the previously filtered results.
 */
export function useMissionListFilters(): MissionListFiltersHook {
  const [searchParams, setSearchParams] = useSearchParams()
  const queryClient = useQueryClient()

  const filtersActive = hasActiveMissionListFilters(searchParams)

  const resetFilters = () => {
    setSearchParams(clearMissionListFilters(searchParams))
    queryClient.invalidateQueries({ queryKey: missionsKeys.all() })
  }

  return { searchParams, setSearchParams, filtersActive, resetFilters }
}
