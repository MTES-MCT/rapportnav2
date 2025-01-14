import axios from "../../../../query-client/axios.ts"
import {useQuery, UseQueryResult} from "@tanstack/react-query"
import {Mission} from "@common/types/mission-types.ts"

interface UseMissionsQueryParams {
    startDateTimeUtc: string
    endDateTimeUtc?: string
}

const useMissionsQuery = ({
                              startDateTimeUtc,
                              endDateTimeUtc,
                          }: UseMissionsQueryParams): UseQueryResult<Mission[], Error> => {
    const fetchMissions = async (): Promise<Mission[]> => {
        const params = new URLSearchParams({startDateTimeUtc})
        if (endDateTimeUtc) {
            params.append("endDateTimeUtc", endDateTimeUtc)

        }
        const response = await axios.get<Mission[]>(`missions?${params.toString()}`)
        return response.data

    }

    const query = useQuery<Mission[], Error>({
        queryKey: ["missions", {startDateTimeUtc, endDateTimeUtc}],
        queryFn: fetchMissions,
        enabled: !!startDateTimeUtc, // Prevents query from running if startDateTimeUtc is not provided
        staleTime: 5 * 60 * 1000, // Cache data for 5 minutes
        retry: 2, // Retry failed requests twice before throwing an error
    })

    return query
}

export default useMissionsQuery
