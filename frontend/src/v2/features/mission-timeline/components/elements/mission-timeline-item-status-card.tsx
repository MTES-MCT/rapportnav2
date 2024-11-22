import { IconProps } from '@mtes-mct/monitor-ui'
import { FC, FunctionComponent } from 'react'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineCardWrapper from '../layout/mission-timeline-item-card-wrapper'
import MissionTimelineItemStatusCardFooter from '../ui/mission-timeline-item-status-card-footer'
import MissionTimelineItemStatusCardTitle from '../ui/mission-timeline-item-status-card-title'
import { MissionTimelineStatusTag } from '../ui/mission-timeline-status-tag'

type MissionTimelineItemStatusCardProps = {
  isSelected?: boolean
  action?: MissionTimelineAction
  icon?: FunctionComponent<IconProps>
  prevAction?: MissionTimelineAction
}
const MissionTimelineItemStatusCard: FC<MissionTimelineItemStatusCardProps> = ({
  icon,
  action,
  prevAction,
  isSelected
}) => {
  return (
    <MissionTimelineCardWrapper
      icon={icon}
      noPadding={true}
      statusTag={<MissionTimelineStatusTag status={action?.status} />}
      title={<MissionTimelineItemStatusCardTitle action={action} isSelected={isSelected} />}
      footer={<MissionTimelineItemStatusCardFooter prevAction={prevAction} />}
    />
  )
}

export default MissionTimelineItemStatusCard
