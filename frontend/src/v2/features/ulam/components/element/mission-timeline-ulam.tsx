import { FC } from 'react'
import MissionTimelineWrapper from '../../../../features/mission-timeline/components/layout/mission-timeline-wrapper'
import useGetActionListQuery from '../../../common/services/use-mission-action-list'
import { useTimeline } from '../../../mission-timeline/hooks/use-timeline'
import MissionTimelineItemUlam from './mission-timeline-item-ulam'

interface MissionTimelineProps {
  missionId: number
}

const MissionTimelineUlam: FC<MissionTimelineProps> = ({ missionId }) => {
  const { getTimeLineAction } = useTimeline()
  const query = useGetActionListQuery(missionId)
  return (
    <MissionTimelineWrapper
      isError={query.error}
      missionId={missionId}
      groupBy="startDateTimeUtc"
      isLoading={query.isLoading}
      item={MissionTimelineItemUlam}
      actions={getTimeLineAction(query.data)}
    />
  )
}

export default MissionTimelineUlam
