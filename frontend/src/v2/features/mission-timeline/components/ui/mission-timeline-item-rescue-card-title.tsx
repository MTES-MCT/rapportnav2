import { FC } from 'react'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineItemCardTitle from './mission-timeline-item-card-title'

const MissionTimelineItemRescueCardTitle: FC<{ action?: MissionTimelineAction }> = ({ action }) => {
  return (
    <MissionTimelineItemCardTitle
      text="Assistance /"
      bold={`${action?.isPersonRescue ? 'Sauvegarde de la vie humaine' : 'Assistance de navire en difficulté'}`}
    />
  )
}

export default MissionTimelineItemRescueCardTitle
