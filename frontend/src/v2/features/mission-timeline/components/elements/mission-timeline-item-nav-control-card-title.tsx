import { Action, ActionControl } from '@common/types/action-types'
import { controlMethodToHumanString, vesselTypeToHumanString } from '@common/utils/control-utils'
import { FC } from 'react'
import MissionTimelineItemCardTitle from '../ui/mission-timeline-item-card-title'

const MissionTimelineItemNavControlCardTitle: FC<{ action?: Action }> = ({ action }) => {
  const data = action?.data as unknown as ActionControl
  return (
    <MissionTimelineItemCardTitle
      text="Contrôles"
      bold={`${controlMethodToHumanString(data?.controlMethod)} - ${vesselTypeToHumanString(data?.vesselType)}`}
    />
  )
}

export default MissionTimelineItemNavControlCardTitle
