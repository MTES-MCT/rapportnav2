import { store } from '..'
import { CompletenessForStats, CompletenessForStatsStatusEnum } from '../../features/common/types/mission-types'

export const setTimelineCompleteForStats = (completeForStats: CompletenessForStats) => {
  store.setState(state => {
    return {
      ...state,
      timeline: {
        ...state.timeline,
        completeForStats,
        isCompleteForStats: completeForStats.status === CompletenessForStatsStatusEnum.VALID
      }
    }
  })
}

export const setTimelineCurrentIndex = (currentIndex: number) => {
  store.setState(state => {
    return {
      ...state,
      timeline: {
        ...state.timeline,
        currentIndex
      }
    }
  })
}
