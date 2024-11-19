import { FC } from 'react'
import { useMissionActionListQuery } from '../../../common/services/use-mission-action-list'
import MissionTimelineWrapper from '../../../mission-timeline/components/layout/mission-timeline-wrapper'
import { useTimeline } from '../../../mission-timeline/hooks/use-timeline'
import MissionTimelineItemPam from './mission-timeline-item-pam'

interface MissionTimelineProps {
  missionId?: number
}

const MissionTimelinePam: FC<MissionTimelineProps> = ({ missionId }) => {
  const { getTimeLineAction } = useTimeline()
  const { data: actions, loading, error } = useMissionActionListQuery(missionId)
  return (
    <MissionTimelineWrapper
      isError={error}
      isLoading={loading}
      missionId={missionId}
      groupBy="startDateTimeUtc"
      item={MissionTimelineItemPam}
      actions={getTimeLineAction(actions)}
    />
  )
}

export default MissionTimelinePam
