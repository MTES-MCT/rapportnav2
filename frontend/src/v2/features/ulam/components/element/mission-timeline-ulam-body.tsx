import { FC } from 'react'
import MissionTimelineWrapper from '../../../mission-timeline/components/layout/mission-timeline-wrapper'
import { MissionTimelineAction } from '../../../mission-timeline/types/mission-timeline-output'
import MissionTimelineItemUlam from './mission-timeline-ulam-item'

interface MissionTimelineBodyProps {
  missionId?: string
  isError?: boolean
  isLoading?: boolean
  noTimelineMessage?: string
  actions: MissionTimelineAction[]
}

const MissionTimelineUlamBody: FC<MissionTimelineBodyProps> = ({
  missionId,
  actions,
  isError,
  isLoading,
  noTimelineMessage
}) => {
  return (
    <MissionTimelineWrapper
      isError={isError}
      actions={actions}
      missionId={missionId}
      isLoading={isLoading}
      groupBy="startDateTimeUtc"
      item={MissionTimelineItemUlam}
      noTimelineMessage={noTimelineMessage}
    />
  )
}

export default MissionTimelineUlamBody
