import { formatMissionActionTypeForHumans } from '@common/types/fish-mission-types'
import { vesselNameOrUnknown } from '@common/utils/action-utils'
import { FC } from 'react'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineItemCardTitle from './mission-timeline-item-card-title'

const MissionTimelineItemFishControlCardTitle: FC<{ action?: MissionTimelineAction }> = ({ action }) => {
  return (
    <MissionTimelineItemCardTitle
      text={`${formatMissionActionTypeForHumans(action?.fishActionType)} - ${vesselNameOrUnknown(action?.vesselName)}`}
    />
  )
}

export default MissionTimelineItemFishControlCardTitle
