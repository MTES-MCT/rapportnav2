import { FormattedControlPlan } from '@common/types/env-mission-types'
import { FC } from 'react'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineItemCardTitle from './mission-timeline-item-card-title'

const MissionTimelineItemEnvControlCardTitle: FC<{ action?: MissionTimelineAction }> = ({ action }) => {
  return (
    <MissionTimelineItemCardTitle
      text="Contrôle"
      bold={
        action && 'formattedControlPlans' in action && !!action?.formattedControlPlans?.length
          ? action?.formattedControlPlans?.map((theme: FormattedControlPlan) => theme.theme).join(', ')
          : 'environnement'
      }
    />
  )
}

export default MissionTimelineItemEnvControlCardTitle
