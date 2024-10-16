import { Action } from '@common/types/action-types'
import { EnvActionControl, FormattedControlPlan } from '@common/types/env-mission-types'
import { FC } from 'react'
import MissionTimelineItemCardTitle from '../ui/mission-timeline-item-card-title'

const MissionTimelineItemSruveillanceCardTitle: FC<{ action?: Action }> = ({ action }) => {
  const data = action?.data as unknown as EnvActionControl
  return (
    <MissionTimelineItemCardTitle
      text="Surveillance"
      bold={
        data?.formattedControlPlans?.length
          ? data?.formattedControlPlans?.map((t: FormattedControlPlan) => t.theme).join(', ')
          : 'environnement marin'
      }
    />
  )
}

export default MissionTimelineItemSruveillanceCardTitle
