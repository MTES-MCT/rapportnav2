import { Mission } from '@features/common/types/mission-types.ts'
import useMissionTimeline from './use-mission-timeline.tsx'

const useMissionDates = (missionId?: string): [Date | undefined, Date | undefined] => {
  const { data: mission } = useMissionTimeline(missionId, 'cache-only')

  try {
    if (!mission) {
      return [undefined, undefined]
    }
    const startDate: string | undefined = (mission as Mission)?.startDateTimeUtc
    const endDate: string | undefined = (mission as Mission)?.endDateTimeUtc

    return [startDate ? new Date(startDate) : undefined, endDate ? new Date(endDate) : undefined]
  } catch (error) {
    return [undefined, undefined]
  }
}

export default useMissionDates
