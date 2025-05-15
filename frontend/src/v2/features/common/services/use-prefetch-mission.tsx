import { useQueryClient } from '@tanstack/react-query'
import { missionsKeys } from './query-keys.ts'
import { DYNAMIC_DATA_STALE_TIME } from '../../../../query-client'
import { fetchMission } from './use-mission.tsx'

/**
 * Returns a function that can be used to prefetch a single Mission by ID.
 */
export function usePrefetchMission() {
  const queryClient = useQueryClient()

  /**
   * Prefetches `/missions/:id` into the cache.
   *
   * @param missionId  numeric mission ID
   */
  const prefetchMission = async (missionId: number) => {
    try {
      await queryClient.prefetchQuery({
        queryKey: missionsKeys.byId(missionId),
        queryFn: () => fetchMission(missionId.toString()),
        staleTime: DYNAMIC_DATA_STALE_TIME,
        networkMode: 'offlineFirst'
      })
    } catch (err) {
      // swallow or log — prefetch is a “nice-to-have”
      console.warn(`prefetchMission(${missionId}) failed`, err)
    }
  }

  return { prefetchMission }
}
