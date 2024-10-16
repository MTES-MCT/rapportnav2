import { Action } from '@common/types/action-types'
import { EnvActionControl, FormattedControlPlan } from '@common/types/env-mission-types'
import { FC } from 'react'
import MissionTimelineItemCardTitle from '../ui/mission-timeline-item-card-title'

const MissionTimelineItemEnvControlCardTitle: FC<{ action?: Action }> = ({ action }) => {
  const data = action?.data as unknown as EnvActionControl
  return (
    <MissionTimelineItemCardTitle
      text="Contrôle"
      bold={
        data && 'formattedControlPlans' in data && !!data?.formattedControlPlans?.length
          ? data?.formattedControlPlans?.map((theme: FormattedControlPlan) => theme.theme).join(', ')
          : 'environnement'
      }
    />
  )
}

export default MissionTimelineItemEnvControlCardTitle
