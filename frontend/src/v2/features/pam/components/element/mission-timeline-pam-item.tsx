import { createElement, FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { ModuleType } from '../../../common/types/module-type'
import MissionTimelineItemWrapper from '../../../mission-timeline/components/layout/mission-timeline-item-wrapper'
import { useTimeline } from '../../../mission-timeline/hooks/use-timeline'
import { MissionTimelineAction } from '../../../mission-timeline/types/mission-timeline-output'
import { usePamTimelineRegistry } from '../../hooks/use-pam-timeline-registry'

interface MissionTimelineItemPamProps {
  missionId?: number
  action: MissionTimelineAction
  prevAction?: MissionTimelineAction
}

const MissionTimelineItemPam: FC<MissionTimelineItemPamProps> = ({ action, prevAction, missionId }) => {
  const { actionId } = useParams()
  const { isIncomplete } = useTimeline()
  const { getTimeline } = usePamTimelineRegistry()
  const [isSelected, setIsSelected] = useState<boolean>(false)

  useEffect(() => {
    setIsSelected(action.id === actionId)
  }, [action, actionId])
  return (
    <MissionTimelineItemWrapper
      action={action}
      missionId={missionId}
      isSelected={isSelected}
      moduleType={ModuleType.PAM}
      isIncomplete={isIncomplete(action)}
      style={getTimeline(action.type).style}
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

export default MissionTimelineItemPam
