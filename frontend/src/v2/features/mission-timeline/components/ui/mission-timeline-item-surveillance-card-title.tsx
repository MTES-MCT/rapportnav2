import { FormattedControlPlan } from '@common/types/env-mission-types'
import { FC } from 'react'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineItemCardTitle from './mission-timeline-item-card-title'

const MissionTimelineItemSurveillanceCardTitle: FC<{ action?: MissionTimelineAction }> = ({ action }) => {
  return (
    <MissionTimelineItemCardTitle
      text="Surveillance"
      bold={
        action?.formattedControlPlans?.length
          ? action?.formattedControlPlans?.map((t: FormattedControlPlan) => t.theme).join(', ')
          : 'environnement marin'
      }
    />
  )
}

export default MissionTimelineItemSurveillanceCardTitle
