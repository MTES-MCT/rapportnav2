import { FC } from 'react'
import MissionTimelineWrapper from '../../../../features/mission-timeline/components/layout/mission-timeline-wrapper'
import { useMissionActionListQuery } from '../../../common/services/use-mission-action-list'
import { useTimeline } from '../../../mission-timeline/hooks/use-timeline'
import MissionTimelineItemUlam from './mission-timeline-item-ulam'

interface MissionTimelineProps {
  missionId?: number
}

const MissionTimelineUlam: FC<MissionTimelineProps> = ({ missionId }) => {
  const { getTimeLineAction } = useTimeline()
  const { data: actions, loading, error } = useMissionActionListQuery(missionId)
  return (
    <MissionTimelineWrapper
      isError={error}
      isLoading={loading}
      missionId={missionId}
      groupBy="startDateTimeUtc"
      item={MissionTimelineItemUlam}
      actions={getTimeLineAction(actions)}
    />
  )
}

export default MissionTimelineUlam
