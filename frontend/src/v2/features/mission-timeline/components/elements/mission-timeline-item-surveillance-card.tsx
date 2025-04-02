import { IconProps } from '@mtes-mct/monitor-ui'
import { FC, FunctionComponent } from 'react'
import TextByCacem from '../../../common/components/ui/text-by-cacem'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineCardWrapper from '../layout/mission-timeline-item-card-wrapper'
import MissionTimelineItemSurveillanceCardTitle from '../ui/mission-timeline-item-surveillance-card-title'

type MissionTimelineItemSurveillanceCardProps = {
  action?: MissionTimelineAction
  icon?: FunctionComponent<IconProps>
}
const MissionTimelineItemSurveillanceCard: FC<MissionTimelineItemSurveillanceCardProps> = ({ icon, action }) => {
  return (
    <MissionTimelineCardWrapper
      action={action}
      icon={icon}
      footer={<TextByCacem />}
      title={<MissionTimelineItemSurveillanceCardTitle action={action} />}
    />
  )
}

export default MissionTimelineItemSurveillanceCard
