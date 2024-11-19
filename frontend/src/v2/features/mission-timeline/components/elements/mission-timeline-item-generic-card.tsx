import { IconProps } from '@mtes-mct/monitor-ui'
import { FC, FunctionComponent } from 'react'
import MissionTimelineCardWrapper from '../layout/mission-timeline-item-card-wrapper'
import MissionTimelineItemCardTitle from '../ui/mission-timeline-item-card-title'

type MissionTimelineItemGenericCardProps = {
  title?: string
  icon?: FunctionComponent<IconProps>
}
const MissionTimelineItemGenericCard: FC<MissionTimelineItemGenericCardProps> = ({ icon, title }) => {
  return <MissionTimelineCardWrapper icon={icon} title={<MissionTimelineItemCardTitle text={title} />} />
}

export default MissionTimelineItemGenericCard
