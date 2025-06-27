import { FC } from 'react'
import MissionPageSectionWrapper from '../../../common/components/layout/mission-page-section-wrapper'
import { useMissionType } from '../../../common/hooks/use-mission-type'
import useGetMissionGeneralInformationQuery from '../../../mission-general-infos/services/use-mission-general-information'
import useGetMissionTimelineQuery from '../../../mission-timeline/services/use-mission-timeline'
import MissionTimelineUlamBody from './mission-timeline-ulam-body'
import MissionTimelineUlamHeader from './mission-timeline-ulam-header'

interface MissionTimelineProps {
  missionId: string
}

const MissionTimelineUlam: FC<MissionTimelineProps> = ({ missionId }) => {
  const { getNoTimelineMessage, isExternalReinforcementTime } = useMissionType()
  const { data: generalInfos } = useGetMissionGeneralInformationQuery(missionId)
  const { data: actions, isError, isLoading } = useGetMissionTimelineQuery(missionId)

  return (
    <MissionPageSectionWrapper
      sectionHeader={
        <MissionTimelineUlamHeader
          missionId={missionId}
          hideAction={isExternalReinforcementTime(generalInfos.missionReportType)}
        />
      }
      sectionBody={
        <MissionTimelineUlamBody
          actions={actions}
          isError={isError}
          isLoading={isLoading}
          missionId={missionId}
          noTimelineMessage={getNoTimelineMessage(generalInfos.missionReportType)}
        />
      }
    />
  )
}

export default MissionTimelineUlam
