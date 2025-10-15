import { MissionSourceEnum } from '@common/types/env-mission-types'
import { IconProps } from '@mtes-mct/monitor-ui'
import { FC, FunctionComponent } from 'react'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineItemEnvControlCard from './mission-timeline-item-env-control-card'
import MissionTimelineItemFishControlCard from './mission-timeline-item-fish-control-card'
import MissionTimelineItemNavControlCard from './mission-timeline-item-nav-control-card'

const MissionTimelineItemControlCard: FC<{
  action?: MissionTimelineAction
  icon?: FunctionComponent<IconProps>
}> = ({ icon, action }) => {
  switch (action?.source) {
    case MissionSourceEnum.MONITORENV:
      return <MissionTimelineItemEnvControlCard icon={icon} action={action} />
    case MissionSourceEnum.MONITORFISH:
      return <MissionTimelineItemFishControlCard icon={icon} action={action} />
    default:
      return <MissionTimelineItemNavControlCard icon={icon} action={action} />
  }
}

export default MissionTimelineItemControlCard
