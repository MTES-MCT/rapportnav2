import { FC } from 'react'
import MissionTimelineWrapper from '../../../../features/mission-timeline/components/layout/mission-timeline-wrapper'
import { useGetMissionTimelineQuery } from '../../../mission-timeline/services/use-mission-timeline'
import MissionTimelineItemUlam from './mission-timeline-item-ulam'

interface MissionTimelineProps {
  missionId?: string
}

const MissionTimelineUlam: FC<MissionTimelineProps> = ({ missionId }) => {
  const { data: mission, loading, error } = useGetMissionTimelineQuery(missionId)
  return (
    <MissionTimelineWrapper
      isError={error}
      isLoading={loading}
      missionId={mission?.id}
      groupBy="startDateTimeUtc"
      item={MissionTimelineItemUlam}
      actions={mission?.actions || []}
    />
  )
}

export default MissionTimelineUlam
