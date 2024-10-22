import { FC } from 'react'
import MissionTimelineWrapper from '../../../mission-timeline/components/layout/mission-timeline-wrapper'
import { useGetMissionTimelineQuery } from '../../../mission-timeline/services/use-mission-timeline'
import MissionTimelineItemPam from './mission-timeline-item-pam'

interface MissionTimelineProps {
  missionId?: string
}

const MissionTimelinePam: FC<MissionTimelineProps> = ({ missionId }) => {
  const { data: mission, loading, error } = useGetMissionTimelineQuery(missionId)
  return (
    <MissionTimelineWrapper
      isError={error}
      isLoading={loading}
      missionId={mission?.id}
      groupBy="startDateTimeUtc"
      item={MissionTimelineItemPam}
      actions={mission?.actions || []}
    />
  )
}

export default MissionTimelinePam
