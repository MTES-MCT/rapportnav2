import { Action } from '@common/types/action-types'
import { MissionSourceEnum } from '@common/types/env-mission-types'
import { FC } from 'react'
import MissionTimelineItemEnvControlCardTitle from './mission-timeline-item-env-control-card-title'
import MissionTimelineItemFishControlCardTitle from './mission-timeline-item-fish-control-card-title'
import MissionTimelineItemNavControlCardTitle from './mission-timeline-item-nav-control-card-title'

const MissionTimelineItemControlCardTitle: FC<{ action?: Action }> = ({ action }) => {
  if (action?.source === MissionSourceEnum.MONITORENV) return <MissionTimelineItemEnvControlCardTitle action={action} />
  if (action?.source === MissionSourceEnum.MONITORFISH)
    return <MissionTimelineItemFishControlCardTitle action={action} />
  return <MissionTimelineItemNavControlCardTitle action={action} />
}

export default MissionTimelineItemControlCardTitle
