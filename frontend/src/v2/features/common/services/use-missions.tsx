import {useQuery, UseQueryResult} from '@tanstack/react-query'
import {Mission2} from '../types/mission-types.ts'
import axios from "../../../../query-client/axios.ts"
import {Mission} from "@common/types/mission-types.ts"
import {missionKeys} from "../../../../query-client/query-keys.tsx";

interface UseMissionsQueryParams {
  startDateTimeUtc: string
  endDateTimeUtc?: string
}

const useMissionsQuery = ({
                            startDateTimeUtc,
                            endDateTimeUtc
                          }: UseMissionsQueryParams): UseQueryResult<Mission2[], Error> => {
  const fetchMissions = async (): Promise<Mission2[]> => {
    const params = new URLSearchParams({startDateTimeUtc})
    if (endDateTimeUtc) {
      params.append('endDateTimeUtc', endDateTimeUtc)
    }
    const response = await axios.get<Mission2[]>(`missions?${params.toString()}`)
    return response.data
  }

  const query = useQuery<Mission[], Error>({
    queryKey: missionKeys.filter(JSON.stringify({startDateTimeUtc, endDateTimeUtc})),
    queryFn: fetchMissions,
    networkMode: 'offlineFirst',
    enabled: !!startDateTimeUtc, // Prevents query from running if startDateTimeUtc is not provided
    staleTime: 5 * 60 * 1000, // Cache data for 5 minutes
    retry: 2, // Retry failed requests twice before throwing an error
  })

  return query
}

export default useMissionsQuery
