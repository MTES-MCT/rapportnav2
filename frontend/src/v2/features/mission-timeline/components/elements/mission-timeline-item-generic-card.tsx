import { IconProps } from '@mtes-mct/monitor-ui'
import { FC, FunctionComponent } from 'react'
import MissionTimelineCardWrapper from '../layout/mission-timeline-item-card-wrapper'
import MissionTimelineItemCardTitle from '../ui/mission-timeline-item-card-title'
import { MissionTimelineAction } from '../../types/mission-timeline-output.ts'

type MissionTimelineItemGenericCardProps = {
  action?: MissionTimelineAction
  title?: string
  icon?: FunctionComponent<IconProps>
}
const MissionTimelineItemGenericCard: FC<MissionTimelineItemGenericCardProps> = ({ action, icon, title }) => {
  return (
    <MissionTimelineCardWrapper
      icon={icon}
      title={<MissionTimelineItemCardTitle text={title} />}
      networkSyncStatus={action?.networkSyncStatus}
    />
  )
}

export default MissionTimelineItemGenericCard
