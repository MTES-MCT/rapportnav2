import useGetMissionQuery from '../../common/services/use-mission'
import { useTimeline } from '../hooks/use-timeline'
import { MissionTimelineAction } from '../types/mission-timeline-output'

type UseMissionTimelineQueryResult<T, M> = {
  data: T
  error: M | null
  isError: boolean
  isPending: boolean
  isLoading: boolean
  isLoadingError: boolean
  isRefetchError: boolean
  isSuccess: boolean
}

const useGetMissionTimelineQuery = (
  missionId?: string
): UseMissionTimelineQueryResult<MissionTimelineAction[], Error> => {
  const { getTimeLineAction } = useTimeline()
  const {
    error,
    isError,
    isPending,
    isLoading,
    isSuccess,
    data: mission,
    isLoadingError,
    isRefetchError
  } = useGetMissionQuery(missionId)
  return {
    error,
    isError,
    isPending,
    isLoading,
    isLoadingError,
    isRefetchError,
    isSuccess,
    data: getTimeLineAction(mission?.actions)
  }
}

export default useGetMissionTimelineQuery
