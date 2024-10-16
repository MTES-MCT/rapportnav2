import { Action } from '@common/types/action-types'
import { FishAction, formatMissionActionTypeForHumans } from '@common/types/fish-mission-types'
import { vesselNameOrUnknown } from '@common/utils/action-utils'
import { FC } from 'react'
import MissionTimelineItemCardTitle from '../ui/mission-timeline-item-card-title'

const MissionTimelineItemFishControlCardTitle: FC<{ action?: Action }> = ({ action }) => {
  const data = action?.data as unknown as FishAction
  return (
    <MissionTimelineItemCardTitle
      text={`${formatMissionActionTypeForHumans(data?.actionType)} - ${vesselNameOrUnknown(data?.vesselName)}`}
    />
  )
}

export default MissionTimelineItemFishControlCardTitle
