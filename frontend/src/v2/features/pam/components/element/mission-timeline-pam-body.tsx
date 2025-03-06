import { FC } from 'react'
import MissionTimelineWrapper from '../../../mission-timeline/components/layout/mission-timeline-wrapper'
import { MissionTimelineAction } from '../../../mission-timeline/types/mission-timeline-output'
import MissionTimelineItemPam from './mission-timeline-pam-item'

interface MissionTimelinePamBodyProps {
  missionId: number
  isLoading: boolean
  isError: Error | null
  actions: MissionTimelineAction[]
}

const MissionTimelinePamBody: FC<MissionTimelinePamBodyProps> = ({ isError, actions, missionId, isLoading }) => {
  return (
    <MissionTimelineWrapper
      isError={isError}
      missionId={missionId}
      groupBy="startDateTimeUtc"
      isLoading={isLoading}
      item={MissionTimelineItemPam}
      actions={actions}
    />
  )
}

export default MissionTimelinePamBody
