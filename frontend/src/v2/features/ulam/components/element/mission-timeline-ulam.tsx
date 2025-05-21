import { FC } from 'react'
import MissionPageSectionWrapper from '../../../common/components/layout/mission-page-section-wrapper'
import useGetMissionTimelineQuery from '../../../mission-timeline/services/use-mission-timeline'
import MissionTimelineUlamBody from './mission-timeline-ulam-body'
import MissionTimelineUlamHeader from './mission-timeline-ulam-header'

interface MissionTimelineProps {
  missionId: string
}

const MissionTimelineUlam: FC<MissionTimelineProps> = ({ missionId }) => {
  const { data: actions, isError, isLoading } = useGetMissionTimelineQuery(missionId)
  return (
    <MissionPageSectionWrapper
      sectionHeader={<MissionTimelineUlamHeader missionId={missionId} />}
      sectionBody={
        <MissionTimelineUlamBody
          actions={actions}
          isError={isError}
          isLoading={isLoading}
          missionId={missionId}
        />
      }
    />
  )
}

export default MissionTimelineUlam
