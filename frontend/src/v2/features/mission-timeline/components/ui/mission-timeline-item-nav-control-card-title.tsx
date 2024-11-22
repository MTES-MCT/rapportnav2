import { controlMethodToHumanString, vesselTypeToHumanString } from '@common/utils/control-utils'
import { FC } from 'react'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineItemCardTitle from './mission-timeline-item-card-title'

const MissionTimelineItemNavControlCardTitle: FC<{ action?: MissionTimelineAction }> = ({ action }) => {
  return (
    <MissionTimelineItemCardTitle
      text="Contrôles"
      bold={`${controlMethodToHumanString(action?.controlMethod)} - ${vesselTypeToHumanString(action?.vesselType)}`}
    />
  )
}

export default MissionTimelineItemNavControlCardTitle
