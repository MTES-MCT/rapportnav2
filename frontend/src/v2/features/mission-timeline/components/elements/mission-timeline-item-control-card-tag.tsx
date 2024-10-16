import { Action } from '@common/types/action-types'
import { ControlType } from '@common/types/control-types'
import { FC } from 'react'
import MissionTimelineIncompleteControlTag from '../ui/mission-timeline-incomplete-control-tag'
import MissionTimelineItemCardTag from '../ui/mission-timeline-item-card-tag'

const MissionTimelineItemControlCardTag: FC<{ action?: Action }> = ({ action }) => {
  const data = action?.data as unknown as { controlsToComplete?: ControlType[] }
  if (data?.controlsToComplete && data?.controlsToComplete?.length > 0) {
    return <MissionTimelineIncompleteControlTag nbrIncompleteControl={data?.controlsToComplete?.length} />
  }
  return <MissionTimelineItemCardTag tags={action?.summaryTags} />
}

export default MissionTimelineItemControlCardTag
