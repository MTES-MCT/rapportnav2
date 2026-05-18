import { CompletenessForStatsStatusEnum, MissionSourceEnum } from '../../common/types/mission-types'
import { MissionTimelineAction } from '../types/mission-timeline-output'

export function useTimelineCompleteForStats() {
  const computeCompleteForStats = (actions: MissionTimelineAction[]) => {
    const sources = new Set<MissionSourceEnum>()

    actions.forEach(action => {
      if (!action.isCompleteForStats && action.source) sources.add(action.source)
    })

    const hasIncomplete = actions.some(
      action => action.completenessForStats?.status === CompletenessForStatsStatusEnum.INCOMPLETE
    )
    const hasInvalid = actions.some(
      action => action.completenessForStats?.status === CompletenessForStatsStatusEnum.INVALID
    )

    let status: CompletenessForStatsStatusEnum
    if (sources.size === 0 && !hasIncomplete && !hasInvalid) {
      status = CompletenessForStatsStatusEnum.VALID
    } else if (hasIncomplete) {
      status = CompletenessForStatsStatusEnum.INCOMPLETE
    } else if (hasInvalid) {
      status = CompletenessForStatsStatusEnum.INVALID
    } else {
      status = CompletenessForStatsStatusEnum.INCOMPLETE
    }

    return {
      sources: Array.from(sources),
      status
    }
  }
  return { computeCompleteForStats }
}
