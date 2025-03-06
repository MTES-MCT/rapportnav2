import { FC } from 'react'
import MissionPageSectionWrapper from '../../../common/components/layout/mission-page-section-wrapper'
import useGetMissionTimelineQuery from '../../../mission-timeline/services/use-mission-timeline'
import MissionTimelinePamBody from './mission-timeline-pam-body'
import MissionTimelinePamHeader from './mission-timeline-pam-header'

interface MissionTimelineProps {
  missionId: number
}

const MissionTimelinePam: FC<MissionTimelineProps> = ({ missionId }) => {
  const { data: actions, error, isLoading } = useGetMissionTimelineQuery(missionId)
  return (
    <MissionPageSectionWrapper
      sectionHeader={<MissionTimelinePamHeader missionId={Number(missionId)} />}
      sectionBody={
        <MissionTimelinePamBody isError={error} actions={actions} isLoading={isLoading} missionId={Number(missionId)} />
      }
    />
  )
}

export default MissionTimelinePam
