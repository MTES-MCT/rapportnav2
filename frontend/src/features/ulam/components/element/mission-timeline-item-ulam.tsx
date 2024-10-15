import MissionTimelineItemWrapper from '@common/components/layout/mission-timeline-item-wrapper'
import MissionTimelineCardWrapper from '@common/components/layout/v2/mission-timeline-item-card-wrapper'
import { MissionTimelineStatusColorTag } from '@common/components/ui/v2/mission-timeline-status-tag-color'
import { Action } from '@common/types/action-types'
import { MissionStatusEnum } from '@common/types/mission-types.ts'
import { ModuleType } from '@common/types/module-type'
import { useUlamActionRegistry } from '@features/ulam/hooks/use-ulam-action-registry'
import { FC } from 'react'
import { useNavigate, useParams } from 'react-router-dom'

interface MissionTimelineItemUlamProps {
  action: Action
  missionId: number
  prevAction?: Action
  missionStatus?: MissionStatusEnum
}

const MissionTimelineItemUlam: FC<MissionTimelineItemUlamProps> = ({ action, prevAction, missionId }) => {
  const navigate = useNavigate()
  const { actionId } = useParams()
  const { style, icon, timeline, isActionStatus } = useUlamActionRegistry(action.type)

  const handleClick = (actionId?: string) => {
    navigate(`/${ModuleType.ULAM}/missions/${missionId}/${actionId}`)
  }

  return (
    <MissionTimelineItemWrapper
      action={action}
      card={
        <MissionTimelineCardWrapper
          icon={icon}
          style={style}
          isSelected={actionId === action.id}
          onClick={() => handleClick(action.id)}
          title={timeline?.getCardTitle(action, actionId === action.id)}
          tags={timeline?.getCardTag ? timeline?.getCardTag(action) : undefined}
          subTitle={timeline?.getCardSubtitle ? timeline.getCardSubtitle(action) : undefined}
          footer={timeline?.getCardFooter ? timeline?.getCardFooter(action, prevAction) : undefined}
          status={isActionStatus ? <MissionTimelineStatusColorTag status={action.status} /> : undefined}
        />
      }
    />
  )
}

export default MissionTimelineItemUlam
