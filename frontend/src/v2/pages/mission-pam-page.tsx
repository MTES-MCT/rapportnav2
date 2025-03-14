import { ULAM_V2_HOME_PATH } from '@router/router.tsx'
import React from 'react'
import { useParams } from 'react-router-dom'
import useAuth from '../features/auth/hooks/use-auth.tsx'
import MissionPageFooterWrapper from '../features/common/components/layout/mission-page-footer-wrapper.tsx'
import MissionPageWrapper from '../features/common/components/layout/mission-page-wrapper.tsx'
import { useMissionReportExport } from '../features/common/hooks/use-mission-report-export.tsx'
import MissionActionPam from '../features/pam/components/element/mission-action-pam.tsx'
import MissionGeneralInformationPam from '../features/pam/components/element/mission-general-information-pam.tsx'
import MissionHeaderPam from '../features/pam/components/element/mission-header-pam.tsx'
import MissionTimelinePam from '../features/pam/components/element/mission-timeline-pam.tsx'

const MissionPamPage: React.FC = () => {
  let { missionId, actionId } = useParams()
  const { navigateAndResetCache } = useAuth()
  const exitMission = async () => navigateAndResetCache(ULAM_V2_HOME_PATH)
  const { exportMission, exportIsLoading } = useMissionReportExport(missionId)

  return (
    <MissionPageWrapper
      missionHeader={
        <MissionHeaderPam
          onClickClose={exitMission}
          onClickExport={exportMission}
          exportLoading={exportIsLoading}
          missionId={Number(missionId)}
        />
      }
      missionGeneralInformations={<MissionGeneralInformationPam missionId={Number(missionId)} />}
      missionTimeLine={<MissionTimelinePam missionId={Number(missionId)} />}
      missionAction={<MissionActionPam missionId={Number(missionId)} actionId={actionId} />}
      missionFooter={<MissionPageFooterWrapper exitMission={exitMission} />}
    />
  )
}

export default MissionPamPage
