import { FC } from 'react'
import useGetActionListQuery from '../../../common/services/use-mission-action-list'
import MissionTimelineWrapper from '../../../mission-timeline/components/layout/mission-timeline-wrapper'
import { useTimeline } from '../../../mission-timeline/hooks/use-timeline'
import MissionTimelineItemPam from './mission-timeline-item-pam'

interface MissionTimelineProps {
  missionId: number
}

const MissionTimelinePam: FC<MissionTimelineProps> = ({ missionId }) => {
  const { getTimeLineAction } = useTimeline()
  const query = useGetActionListQuery(missionId)
  return (
    <MissionTimelineWrapper
      isError={query.error}
      missionId={missionId}
      groupBy="startDateTimeUtc"
      isLoading={query.isLoading}
      item={MissionTimelineItemPam}
      actions={getTimeLineAction(query.data)}
    />
  )
}

export default MissionTimelinePam
