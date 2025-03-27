import React from 'react'
import { ULAM_V2_HOME_PATH } from '@router/routes.tsx'
import { useParams } from 'react-router-dom'
import useAuth from '../features/auth/hooks/use-auth.tsx'
import MissionPageWrapper from '../features/common/components/layout/mission-page-wrapper.tsx'
import MissionPageFooter from '../features/common/components/ui/mission-page-footer.tsx'
import { useMissionReportExport } from '../features/common/hooks/use-mission-report-export.tsx'
import MissionActionPam from '../features/pam/components/element/mission-action-pam.tsx'
import MissionGeneralInformationPam from '../features/pam/components/element/general-info/mission-general-information-pam.tsx'
import MissionHeaderPam from '../features/pam/components/element/mission-header-pam.tsx'
import MissionTimelinePam from '../features/pam/components/element/mission-timeline-pam.tsx'
import { missionsKeys } from '../features/common/services/query-keys.ts'

const MissionPamPage: React.FC = () => {
  let { missionId, actionId } = useParams()
  const { navigateAndResetCache } = useAuth()
  const { exportMission, exportIsLoading } = useMissionReportExport(missionId)
  const exitMission = async () => navigateAndResetCache(ULAM_V2_HOME_PATH, missionsKeys.all())

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
      missionFooter={<MissionPageFooter exitMission={exitMission} />}
    />
  )
}

export default MissionPamPage
