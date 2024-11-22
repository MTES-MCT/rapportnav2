import { MissionSourceEnum } from '@common/types/env-mission-types'
import { FC } from 'react'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineItemEnvControlCardTitle from '../ui/mission-timeline-item-env-control-card-title'
import MissionTimelineItemFishControlCardTitle from '../ui/mission-timeline-item-fish-control-card-title'
import MissionTimelineItemNavControlCardTitle from '../ui/mission-timeline-item-nav-control-card-title'

const MissionTimelineItemControlCardTitle: FC<{ action?: MissionTimelineAction }> = ({ action }) => {
  if (action?.source === MissionSourceEnum.MONITORENV) return <MissionTimelineItemEnvControlCardTitle action={action} />
  if (action?.source === MissionSourceEnum.MONITORFISH)
    return <MissionTimelineItemFishControlCardTitle action={action} />
  return <MissionTimelineItemNavControlCardTitle action={action} />
}

export default MissionTimelineItemControlCardTitle
