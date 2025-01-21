import { FC } from 'react'
import { MissionAction } from '../../../common/types/mission-action'
import MissionTimelineWrapper from '../../../mission-timeline/components/layout/mission-timeline-wrapper'
import { useTimeline } from '../../../mission-timeline/hooks/use-timeline'
import MissionTimelineItemPam from './mission-timeline-item-pam'

interface MissionTimelineProps {
  missionId: number
  isLoading: boolean
  isError: Error | null
  actions?: MissionAction[]
}

const MissionTimelinePam: FC<MissionTimelineProps> = ({ isError, actions, missionId, isLoading }) => {
  const { getTimeLineAction } = useTimeline()
  return (
    <MissionTimelineWrapper
      isError={isError}
      missionId={missionId}
      groupBy="startDateTimeUtc"
      isLoading={isLoading}
      item={MissionTimelineItemPam}
      actions={getTimeLineAction(actions)}
    />
  )
}

export default MissionTimelinePam
