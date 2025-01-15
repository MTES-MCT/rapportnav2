import { CompletenessForStatsStatusEnum, MissionSourceEnum } from '../../common/types/mission-types'
import { MissionTimelineAction } from '../types/mission-timeline-output'

export function useTimelineCompleteForStats() {
  const computeCompleteForStats = (actions: MissionTimelineAction[]) => {
    const sources = new Set<MissionSourceEnum>()

    actions.forEach(action => {
      if (!action.isCompleteForStats && action.source) sources.add(action.source)
    })
    return {
      sources: Array.from(sources),
      status: sources.size === 0 ? CompletenessForStatsStatusEnum.COMPLETE : CompletenessForStatsStatusEnum.INCOMPLETE
    }
  }
  return { computeCompleteForStats }
}
