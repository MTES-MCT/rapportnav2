import { Action, ActionRescue } from '@common/types/action-types'
import { FC } from 'react'
import MissionTimelineItemCardTitle from '../ui/mission-timeline-item-card-title'

const MissionTimelineItemRescueCardTitle: FC<{ action?: Action }> = ({ action }) => {
  const data = action?.data as unknown as ActionRescue
  return (
    <MissionTimelineItemCardTitle
      truncate
      text="Assistance /"
      bold={`${data?.isPersonRescue ? 'Sauvegarde de la vie humaine' : 'Assistance de navire en difficulté'}`}
    />
  )
}

export default MissionTimelineItemRescueCardTitle
