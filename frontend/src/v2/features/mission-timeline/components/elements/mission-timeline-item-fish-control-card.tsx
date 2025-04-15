import { IconProps } from '@mtes-mct/monitor-ui'
import { FC, FunctionComponent } from 'react'
import TextByCnsp from '../../../common/components/ui/text-by-cnsp'
import { MissionTimelineAction } from '../../types/mission-timeline-output'
import MissionTimelineCardWrapper from '../layout/mission-timeline-item-card-wrapper'
import MissionTimelineItemControlCardSubtitle from '../ui/mission-timeline-item-control-card-Subtitle'
import MissionTimelineItemControlCardTag from '../ui/mission-timeline-item-control-card-tag'
import MissionTimelineItemFishControlCardTitle from '../ui/mission-timeline-item-fish-control-card-title'

type MissionTimelineItemFishControlCardProps = {
  action?: MissionTimelineAction
  icon?: FunctionComponent<IconProps>
}
const MissionTimelineItemFishControlCard: FC<MissionTimelineItemFishControlCardProps> = ({ icon, action }) => {
  return (
    <MissionTimelineCardWrapper
      icon={icon}
      title={<MissionTimelineItemFishControlCardTitle action={action} />}
      tags={<MissionTimelineItemControlCardTag action={action} />}
      subTitle={<MissionTimelineItemControlCardSubtitle action={action} />}
      footer={<TextByCnsp />}
      actionType={action?.type}
      networkSyncStatus={action?.networkSyncStatus}
    />
  )
}

export default MissionTimelineItemFishControlCard
