import { skipToken, useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios.ts'
import { DYNAMIC_DATA_STALE_TIME } from '../../../../query-client/index.ts'
import { inquiriesKeys } from '../../common/services/query-keys.ts'
import { Inquiry } from '../../common/types/inquiry.ts'

const useGetInquiryQuery = (inquiryId?: string) => {
  const fetchInquiry = (): Promise<Inquiry> => axios.get(`inquiries/${inquiryId}`).then(response => response.data)

  const query = useQuery<Inquiry>({
    queryKey: inquiriesKeys.byId(inquiryId!),
    queryFn: inquiryId ? () => fetchInquiry() : skipToken,
    staleTime: DYNAMIC_DATA_STALE_TIME, // Cache data for 5 minutes
    retry: 2 // Retry failed requests twice before throwing an error
  })

  return query
}

export default useGetInquiryQuery
