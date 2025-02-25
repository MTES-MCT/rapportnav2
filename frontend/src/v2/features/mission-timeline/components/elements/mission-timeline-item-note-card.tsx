import { IconProps } from '@mtes-mct/monitor-ui'
import { FC, FunctionComponent } from 'react'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineCardWrapper from '../layout/mission-timeline-item-card-wrapper'
import MissionTimelineItemNoteCardTitle from '../ui/mission-timeline-item-note-card-title'

type MissionTimelineItemNoteCardProps = {
  title?: string
  action?: MissionTimelineAction
  icon?: FunctionComponent<IconProps>
}
const MissionTimelineItemNoteCard: FC<MissionTimelineItemNoteCardProps> = ({ icon, title, action }) => {
  return (
    <MissionTimelineCardWrapper icon={icon} title={<MissionTimelineItemNoteCardTitle text={title} action={action} />} />
  )
}

export default MissionTimelineItemNoteCard
