import { IconProps } from '@mtes-mct/monitor-ui'
import { FC, FunctionComponent } from 'react'
import TextByCacem from '../../../common/components/ui/text-by-cacem'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineCardWrapper from '../layout/mission-timeline-item-card-wrapper'
import MissionTimelineItemControlCardSubtitle from '../ui/mission-timeline-item-control-card-Subtitle'
import MissionTimelineItemControlCardTag from '../ui/mission-timeline-item-control-card-tag'
import MissionTimelineItemEnvControlCardTitle from '../ui/mission-timeline-item-env-control-card-title'

type MissionTimelineItemEnvControlCardProps = {
  action?: MissionTimelineAction
  icon?: FunctionComponent<IconProps>
}
const MissionTimelineItemEnvControlCard: FC<MissionTimelineItemEnvControlCardProps> = ({ icon, action }) => {
  return (
    <MissionTimelineCardWrapper
      icon={icon}
      title={<MissionTimelineItemEnvControlCardTitle action={action} />}
      tags={<MissionTimelineItemControlCardTag action={action} />}
      subTitle={<MissionTimelineItemControlCardSubtitle action={action} />}
      footer={<TextByCacem />}
    />
  )
}

export default MissionTimelineItemEnvControlCard
