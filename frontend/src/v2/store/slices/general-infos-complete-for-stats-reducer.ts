import { store } from '..'
import { CompletenessForStats, CompletenessForStatsStatusEnum } from '../../features/common/types/mission-types'

export const setTimelineCompleteForStats = (completeForStats: CompletenessForStats) => {
  store.setState(state => {
    return {
      ...state,
      generalInformations: {
        completeForStats,
        isCompleteForStats: completeForStats.status === CompletenessForStatsStatusEnum.COMPLETE
      }
    }
  })
}
