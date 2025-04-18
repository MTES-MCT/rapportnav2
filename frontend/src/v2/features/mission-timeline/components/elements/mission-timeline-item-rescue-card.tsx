import { IconProps } from '@mtes-mct/monitor-ui'
import { FC, FunctionComponent } from 'react'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineCardWrapper from '../layout/mission-timeline-item-card-wrapper'
import MissionTimelineItemRescueCardTitle from '../ui/mission-timeline-item-rescue-card-title'

type MissionTimelineItemRescueCardProps = {
  action?: MissionTimelineAction
  icon?: FunctionComponent<IconProps>
}
const MissionTimelineItemRescueCard: FC<MissionTimelineItemRescueCardProps> = ({ icon, action }) => {
  return (
    <MissionTimelineCardWrapper
      icon={icon}
      title={<MissionTimelineItemRescueCardTitle action={action} />}
      actionType={action?.type}
      networkSyncStatus={action?.networkSyncStatus}
    />
  )
}

export default MissionTimelineItemRescueCard
