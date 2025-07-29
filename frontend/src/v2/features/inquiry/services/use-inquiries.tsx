import { useQuery, useQueryClient, UseQueryResult } from '@tanstack/react-query'
import { useEffect } from 'react'
import axios from '../../../../query-client/axios.ts'
import { DYNAMIC_DATA_STALE_TIME } from '../../../../query-client/index.ts'
import { inquiriesKeys } from '../../common/services/query-keys.ts'
import { Inquiry } from '../../common/types/inquiry.ts'

const useInquiriesQuery = (params: URLSearchParams): UseQueryResult<Inquiry[], Error> => {
  const queryClient = useQueryClient()

  const fetchInquiries = async (): Promise<Inquiry[]> => {
    const response = await axios.get<Inquiry[]>(`inquiries?${params.toString()}`)
    return response.data
  }

  const endDateTimeUtc = params.get('endDateTimeUtc')
  const startDateTimeUtc = params.get('startDateTimeUtc')

  const query = useQuery<Inquiry[], Error>({
    queryKey: inquiriesKeys.filter(JSON.stringify({ startDateTimeUtc, endDateTimeUtc })),
    queryFn: fetchInquiries,
    enabled: !!endDateTimeUtc && !!startDateTimeUtc, // Prevents query from running if startDateTimeUtc is not provided
    staleTime: DYNAMIC_DATA_STALE_TIME, // Cache data for 5 minutes
    retry: 2 // Retry failed requests twice before throwing an error,
  })

  useEffect(() => {
    if (!query.data) return
    else {
      ;(query.data || []).forEach((inquiry: Inquiry) => {
        if (inquiry.id) queryClient.setQueryData(inquiriesKeys.byId(inquiry.id), inquiry)
      })
    }
  }, [query.data, queryClient])

  return query
}

export default useInquiriesQuery
