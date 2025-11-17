import { FC } from 'react'
import PageSectionWrapper from '../../../../common/components/layout/page-section-wrapper.tsx'
import useGetMissionTimelineQuery from '../../../../mission-timeline/services/use-mission-timeline.tsx'
import MissionTimelinePamBody from './mission-timeline-pam-body.tsx'
import MissionTimelinePamHeader from './mission-timeline-pam-header.tsx'

interface MissionTimelineProps {
  missionId: string
}

const MissionTimelinePam: FC<MissionTimelineProps> = ({ missionId }) => {
  const { data: actions, error, isLoading } = useGetMissionTimelineQuery(missionId)
  return (
    <PageSectionWrapper
      sectionHeader={<MissionTimelinePamHeader missionId={Number(missionId)} />}
      sectionBody={
        <MissionTimelinePamBody isError={error} actions={actions} isLoading={isLoading} missionId={Number(missionId)} />
      }
    />
  )
}

export default MissionTimelinePam
