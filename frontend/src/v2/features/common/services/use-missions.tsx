import { useQuery, useQueryClient, UseQueryResult } from '@tanstack/react-query'
import { useEffect } from 'react'
import { DYNAMIC_DATA_STALE_TIME } from '../../../../query-client'
import axios from '../../../../query-client/axios.ts'
import { Mission2 } from '../types/mission-types.ts'
import { missionsKeys } from './query-keys.ts'

const useMissionsQuery = (params: URLSearchParams): UseQueryResult<Mission2[], Error> => {
  const queryClient = useQueryClient()

  const fetchMissions = async (): Promise<Mission2[]> => {
    const response = await axios.get<Mission2[]>(`missions?${params.toString()}`)
    return response.data
  }

  const endDateTimeUtc = params.get('endDateTimeUtc')
  const startDateTimeUtc = params.get('startDateTimeUtc')

  const query = useQuery<Mission2[], Error>({
    queryKey: missionsKeys.filter(JSON.stringify({ startDateTimeUtc, endDateTimeUtc })),
    queryFn: fetchMissions,
    enabled: !!endDateTimeUtc && !!startDateTimeUtc, // Prevents query from running if startDateTimeUtc is not provided
    staleTime: DYNAMIC_DATA_STALE_TIME, // Cache data for 5 minutes
    retry: 2 // Retry failed requests twice before throwing an error,
  })

  useEffect(() => {
    if (!query.data) return
    else {
      // for offline mode, preset the mission in their individual cache keys
      ;(query.data || []).forEach((mission: Mission2) => {
        queryClient.setQueryData(missionsKeys.byId(mission.id), mission)
      })
    }
  }, [query.data, queryClient])

  return query
}

export default useMissionsQuery
