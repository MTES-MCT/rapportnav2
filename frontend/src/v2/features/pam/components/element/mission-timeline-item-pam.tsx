import { createElement, FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { ModuleType } from '../../../common/types/module-type'
import MissionTimelineItemWrapper from '../../../mission-timeline/components/layout/mission-timeline-item-wrapper'
import { MissionTimelineAction } from '../../../mission-timeline/types/mission-timeline-output'
import { usePamActionRegistry } from '../../hooks/use-pam-action-registry'

interface MissionTimelineItemPamProps {
  missionId?: number
  action: MissionTimelineAction
  prevAction?: MissionTimelineAction
}

const MissionTimelineItemPam: FC<MissionTimelineItemPamProps> = ({ action, prevAction, missionId }) => {
  const { actionId } = useParams()
  const [isSelected, setIsSelected] = useState<boolean>(false)
  const { style, icon, title, timeline, hasStatusTag, isIncomplete } = usePamActionRegistry(action.type)

  useEffect(() => {
    setIsSelected(action.id === actionId)
  }, [action, actionId])
  return (
    <MissionTimelineItemWrapper
      style={style}
      action={action}
      missionId={missionId}
      isSelected={isSelected}
      moduleType={ModuleType.PAM}
      card={createElement(timeline.component, { icon, title, action, prevAction, isSelected })}
      isIncomplete={isIncomplete(action)}
    />
  )
}

export default MissionTimelineItemPam
