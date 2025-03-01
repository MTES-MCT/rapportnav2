import { ULAM_V2_HOME_PATH } from '@router/router.tsx'
import React from 'react'
import { useParams } from 'react-router-dom'
import useAuth from '../features/auth/hooks/use-auth.tsx'
import MissionPageFooterWrapper from '../features/common/components/layout/mission-page-footer-wrapper.tsx'
import MissionPageHeaderWrapper from '../features/common/components/layout/mission-page-header-wrapper.tsx'
import MissionPageSectionWrapper from '../features/common/components/layout/mission-page-section-wrapper.tsx'
import MissionPageWrapper from '../features/common/components/layout/mission-page-wrapper.tsx'
import MissionGeneralInformationHeader from '../features/common/components/ui/mission-general-information-header.tsx'
import MissionPageError from '../features/common/components/ui/mission-page-error.tsx'
import MissionPageLoading from '../features/common/components/ui/mission-page-loading.tsx'
import { useMissionReportExport } from '../features/common/hooks/use-mission-report-export.tsx'
import useGetMissionQuery from '../features/common/services/use-mission.tsx'
import MissionActionUlam from '../features/ulam/components/element/mission-action-ulam.tsx'
import MissionGeneralInformationUlam from '../features/ulam/components/element/mission-general-information-ulam.tsx'
import MissionTimelineHeaderUlam from '../features/ulam/components/element/mission-timeline-header-ulam.tsx'
import MissionTimelineUlam from '../features/ulam/components/element/mission-timeline-ulam.tsx'

const MissionUlamPage: React.FC = () => {
  let { missionId, actionId } = useParams()
  const { navigateAndResetCache } = useAuth()
  const exitMission = async () => navigateAndResetCache(ULAM_V2_HOME_PATH)

  const { exportMission, exportIsLoading } = useMissionReportExport()
  const { data: mission, isLoading, error } = useGetMissionQuery(missionId)

  if (error) return <MissionPageError />
  if (isLoading) return <MissionPageLoading />

  return (
    <MissionPageWrapper
      missionHeader={
        <MissionPageHeaderWrapper
          mission={mission}
          onClickClose={exitMission}
          onClickExport={exportMission}
          exportLoading={exportIsLoading}
        />
      }
      missionGeneralInformations={
        <MissionPageSectionWrapper
          sectionHeader={<MissionGeneralInformationHeader />}
          sectionBody={<MissionGeneralInformationUlam mission={mission} />}
        />
      }
      missionTimeLine={
        <MissionPageSectionWrapper
          sectionHeader={<MissionTimelineHeaderUlam missionId={Number(missionId)} />}
          sectionBody={
            <MissionTimelineUlam
              isError={error}
              isLoading={isLoading}
              actions={mission?.actions}
              missionId={Number(missionId)}
            />
          }
        />
      }
      missionAction={
        <MissionActionUlam missionId={Number(missionId)} actionId={actionId} missionStatus={mission?.status} />
      }
      missionFooter={<MissionPageFooterWrapper exitMission={exitMission} />}
    />
  )
}

export default MissionUlamPage
