import { createElement, FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import TimelineItemWrapper from '../../../common/components/layout/timeline-item-wrapper'
import { useTimeline } from '../../../mission-timeline/hooks/use-timeline'
import { MissionTimelineAction } from '../../../mission-timeline/types/mission-timeline-output'
import { useUlamTimelineRegistry } from '../../hooks/use-ulam-timeline-registry'

interface MissionTimelineUlamItemProps {
  baseUrl: string
  action: MissionTimelineAction
  prevAction?: MissionTimelineAction
}

const MissionTimelineUlamItem: FC<MissionTimelineUlamItemProps> = ({ action, prevAction, baseUrl }) => {
  const { actionId } = useParams()
  const { isIncomplete } = useTimeline()
  const { getTimeline } = useUlamTimelineRegistry()
  const [isSelected, setIsSelected] = useState<boolean>(false)

  useEffect(() => {
    setIsSelected(action.id === actionId)
  }, [action, actionId])

  return (
    <TimelineItemWrapper
      action={action}
      baseUrl={baseUrl}
      isSelected={isSelected}
      isIncomplete={isIncomplete(action)}
      style={getTimeline(action.type)?.style}
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

export default MissionTimelineUlamItem
