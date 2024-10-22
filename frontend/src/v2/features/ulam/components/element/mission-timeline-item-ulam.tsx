import { Action } from '@common/types/action-types'
import { FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { ModuleType } from '../../../../features/common/types/module-type'
import MissionTimelineCardWrapper from '../../../mission-timeline/components/layout/mission-timeline-item-card-wrapper'
import MissionTimelineItemWrapper from '../../../mission-timeline/components/layout/mission-timeline-item-wrapper'
import { MissionTimelineStatusTag } from '../../../mission-timeline/components/ui/mission-timeline-status-tag'
import { useUlamActionRegistry } from '../../hooks/use-ulam-action-registry'

interface MissionTimelineItemUlamProps {
  action: Action
  missionId?: number
  prevAction?: Action
}

const MissionTimelineItemUlam: FC<MissionTimelineItemUlamProps> = ({ action, prevAction, missionId }) => {
  const { actionId } = useParams()
  const [isSelected, setIsSelected] = useState<boolean>(false)
  const { style, icon, timeline, hasStatusTag, isIncomplete } = useUlamActionRegistry(action.type)

  useEffect(() => {
    setIsSelected(action.id === actionId)
  }, [action, actionId])
  return (
    <MissionTimelineItemWrapper
      style={style}
      action={action}
      missionId={missionId}
      isSelected={isSelected}
      moduleType={ModuleType.ULAM}
      card={
        <MissionTimelineCardWrapper
          icon={icon}
          title={timeline?.getCardTitle(action, isSelected)}
          tags={timeline?.getCardTag ? timeline?.getCardTag(action) : undefined}
          subTitle={timeline?.getCardSubtitle ? timeline.getCardSubtitle(action) : undefined}
          footer={timeline?.getCardFooter ? timeline?.getCardFooter(action, prevAction) : undefined}
          statusTag={hasStatusTag ? <MissionTimelineStatusTag status={action.status} /> : undefined}
        />
      }
      isIncomplete={isIncomplete(action)}
    />
  )
}

export default MissionTimelineItemUlam
