import { useTimeline } from '../../mission-timeline/hooks/use-timeline'
import { TimelineAction } from '../../mission-timeline/types/mission-timeline-output'
import useGetInquiryQuery from '../services/use-inquiry'

type UseInquiryTimelineQueryResult<T, M> = {
  data: T
  error: M | null
  isError: boolean
  isPending: boolean
  isLoading: boolean
  isLoadingError: boolean
  isRefetchError: boolean
  isSuccess: boolean
}

const useGetInquiryTimelineQuery = (inquiryId?: string): UseInquiryTimelineQueryResult<TimelineAction[], Error> => {
  const { getTimeLineAction } = useTimeline()
  const {
    error,
    isError,
    isPending,
    isLoading,
    isSuccess,
    data: inquiry,
    isLoadingError,
    isRefetchError
  } = useGetInquiryQuery(inquiryId)
  return {
    error,
    isError,
    isPending,
    isLoading,
    isLoadingError,
    isRefetchError,
    isSuccess,
    data: getTimeLineAction(inquiry?.actions)
  }
}

export default useGetInquiryTimelineQuery
