import { createElement, FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { ModuleType } from '../../../../features/common/types/module-type'
import MissionTimelineItemWrapper from '../../../mission-timeline/components/layout/mission-timeline-item-wrapper'
import { MissionTimelineAction } from '../../../mission-timeline/types/mission-timeline-output'
import { useUlamActionRegistry } from '../../hooks/use-ulam-action-registry'

interface MissionTimelineItemUlamProps {
  missionId?: number
  action: MissionTimelineAction
  prevAction?: MissionTimelineAction
}

const MissionTimelineItemUlam: FC<MissionTimelineItemUlamProps> = ({ action, prevAction, missionId }) => {
  const { actionId } = useParams()
  const [isSelected, setIsSelected] = useState<boolean>(false)
  const { style, icon, timeline, title, isIncomplete } = useUlamActionRegistry(action.type)

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
      card={createElement(timeline.component, { icon, title, action, prevAction, isSelected })}
      isIncomplete={isIncomplete(action)}
    />
  )
}

export default MissionTimelineItemUlam
