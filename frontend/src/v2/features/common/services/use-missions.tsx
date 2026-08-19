import { useQuery, UseQueryResult } from '@tanstack/react-query'
import { DYNAMIC_DATA_STALE_TIME } from '../../../../query-client'
import axios from '../../../../query-client/axios.ts'
import { MissionListData } from '../types/mission-types.ts'
import { missionsKeys } from './query-keys.ts'
import { useOnlineManager } from '../hooks/use-online-manager.tsx'

const useMissionsQuery = (params: URLSearchParams): UseQueryResult<MissionListData[], Error> => {
  const { isOnline } = useOnlineManager()

  const fetchMissions = async (): Promise<MissionListData[]> => {
    const response = await axios.get<MissionListData[]>(`missions?${params.toString()}`)
    return response.data
  }

  const endDateTimeUtc = params.get('endDateTimeUtc')
  const startDateTimeUtc = params.get('startDateTimeUtc')

  return useQuery<MissionListData[], Error>({
    queryKey: missionsKeys.filter(JSON.stringify({ startDateTimeUtc, endDateTimeUtc })),
    // The list now returns a light payload (no actions, no full general info), so we no longer prefill the
    // per-mission (`missionsKeys.byId`) or per-action caches from it — the detail page fetches the full
    // mission via its own `byId` query.
    queryFn: fetchMissions,
    enabled: !!endDateTimeUtc && !!startDateTimeUtc && isOnline, // Prevents query from running if startDateTimeUtc is not provided
    staleTime: DYNAMIC_DATA_STALE_TIME, // Cache data for 3 minutes
    retry: 2, // Retry failed requests twice before throwing an error,
    refetchInterval: DYNAMIC_DATA_STALE_TIME
  })
}

export default useMissionsQuery
