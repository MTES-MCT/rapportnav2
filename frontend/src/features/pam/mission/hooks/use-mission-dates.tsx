import { Mission, MissionStatusEnum } from '../../../common/types/mission-types.ts'
import client from '../../../../apollo-client/apollo-client.ts'
import { GET_MISSION_TIMELINE } from './use-mission-timeline.tsx'
import useMissionExcerpt from '@features/pam/mission/hooks/use-mission-excerpt.tsx'
import { WatchQueryFetchPolicy } from '@apollo/client/core/watchQueryOptions'

const useMissionDates = (missionId?: string): [Date | undefined, Date | undefined] | undefined => {
  try {
    const { mission } = useMissionExcerpt('11', 'cache-only')
    debugger
    const aa = new Date((mission as Mission)?.startDateTimeUtc)
    return [(mission as Mission)?.startDateTimeUtc, (mission as Mission)?.endDateTimeUtc]
  } catch (error) {
    return undefined
  }
}

export default useMissionDates
