import { IconProps } from '@mtes-mct/monitor-ui'
import { FC, FunctionComponent } from 'react'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineCardWrapper from '../layout/mission-timeline-item-card-wrapper'
import MissionTimelineItemControlCardSubtitle from '../ui/mission-timeline-item-control-card-Subtitle'
import MissionTimelineItemControlCardTag from '../ui/mission-timeline-item-control-card-tag'
import MissionTimelineItemNavControlCardTitle from '../ui/mission-timeline-item-nav-control-card-title'

type MissionTimelineItemNavControlCardProps = {
  action?: MissionTimelineAction
  icon?: FunctionComponent<IconProps>
}
const MissionTimelineItemNavControlCard: FC<MissionTimelineItemNavControlCardProps> = ({ icon, action }) => {
  return (
    <MissionTimelineCardWrapper
      action={action}
      icon={icon}
      title={<MissionTimelineItemNavControlCardTitle action={action} />}
      tags={<MissionTimelineItemControlCardTag action={action} />}
      subTitle={<MissionTimelineItemControlCardSubtitle action={action} />}
    />
  )
}

export default MissionTimelineItemNavControlCard
