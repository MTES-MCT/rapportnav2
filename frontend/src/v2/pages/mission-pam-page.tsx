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
import MissionActionPam from '../features/pam/components/element/mission-action-pam.tsx'
import MissionTimelineHeaderPam from '../features/pam/components/element/mission-timeline-header-pam.tsx'
import MissionTimelinePam from '../features/pam/components/element/mission-timeline-pam.tsx'

const MissionPamPage: React.FC = () => {
  let { missionId, actionId } = useParams()
  const { navigateAndResetCache } = useAuth()
  const exitMission = async () => navigateAndResetCache(ULAM_V2_HOME_PATH)

  const { data: mission, isLoading, error } = useGetMissionQuery(missionId)
  const { exportMission, exportIsLoading } = useMissionReportExport(missionId)

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
          sectionBody={<>General infos pam</>}
        />
      }
      missionTimeLine={
        <MissionPageSectionWrapper
          sectionHeader={<MissionTimelineHeaderPam missionId={Number(missionId)} />}
          sectionBody={
            <MissionTimelinePam
              isError={error}
              isLoading={isLoading}
              actions={mission?.actions}
              missionId={Number(missionId)}
            />
          }
        />
      }
      missionAction={
        <MissionActionPam missionId={Number(missionId)} actionId={actionId} missionStatus={mission?.status} />
      }
      missionFooter={<MissionPageFooterWrapper exitMission={exitMission} />}
    />
  )
}

export default MissionPamPage
