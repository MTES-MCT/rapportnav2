import { FC } from 'react'
import MissionIncompleteControlTag from '../../../common/components/ui/mission-incomplete-control-tag'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineItemCardTag from './mission-timeline-item-card-tag'

const MissionTimelineItemControlCardTag: FC<{ action?: MissionTimelineAction }> = ({ action }) => {
  if (action?.controlsToComplete && action?.controlsToComplete?.length > 0) {
    return <MissionIncompleteControlTag nbrIncompleteControl={action?.controlsToComplete?.length} />
  }
  return <MissionTimelineItemCardTag tags={action?.summaryTags} />
}

export default MissionTimelineItemControlCardTag
