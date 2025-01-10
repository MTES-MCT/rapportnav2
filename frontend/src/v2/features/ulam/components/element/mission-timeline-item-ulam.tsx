import { createElement, FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { ModuleType } from '../../../../features/common/types/module-type'
import MissionTimelineItemWrapper from '../../../mission-timeline/components/layout/mission-timeline-item-wrapper'
import { useTimeline } from '../../../mission-timeline/hooks/use-timeline'
import { MissionTimelineAction } from '../../../mission-timeline/types/mission-timeline-output'
import { useUlamTimelineRegistry } from '../../hooks/use-ulam-timeline-registry'

interface MissionTimelineItemUlamProps {
  missionId?: number
  action: MissionTimelineAction
  prevAction?: MissionTimelineAction
}

const MissionTimelineItemUlam: FC<MissionTimelineItemUlamProps> = ({ action, prevAction, missionId }) => {
  const { actionId } = useParams()
  const { isIncomplete } = useTimeline()
  const { getTimeline } = useUlamTimelineRegistry()
  const [isSelected, setIsSelected] = useState<boolean>(false)

  useEffect(() => {
    setIsSelected(action.id === actionId)
  }, [action, actionId])
  return (
    <MissionTimelineItemWrapper
      style={getTimeline(action.type)?.style}
      action={action}
      missionId={missionId}
      isSelected={isSelected}
      moduleType={ModuleType.ULAM}
      isIncomplete={isIncomplete(action)}
      card={createElement(getTimeline(action.type).component, {
        icon: getTimeline(action.type).icon,
        title: getTimeline(action.type).title,
        action,
        prevAction,
        isSelected
      })}
    />
  )
}

export default MissionTimelineItemUlam
