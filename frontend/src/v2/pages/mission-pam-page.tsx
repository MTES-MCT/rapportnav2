import { PAM_V2_HOME_PATH } from '@router/routes.tsx'
import React from 'react'
import { useParams } from 'react-router-dom'
import useAuth from '../features/auth/hooks/use-auth.tsx'
import MissionPageWrapper from '../features/common/components/layout/mission-page-wrapper.tsx'
import MissionPageFooter from '../features/common/components/ui/mission-page-footer.tsx'
import { missionsKeys } from '../features/common/services/query-keys.ts'
import MissionGeneralInformationPam from '../features/pam/components/element/general-info/mission-general-information-pam.tsx'
import MissionActionPam from '../features/pam/components/element/mission-action-pam.tsx'
import MissionHeaderPam from '../features/pam/components/element/mission-header-pam.tsx'
import MissionTimelinePam from '../features/pam/components/element/mission-timeline-pam.tsx'
import OfflineDialog from '../features/pam/components/ui/offline-dialog.tsx'

const MissionPamPage: React.FC = () => {
  let { missionId, actionId } = useParams()
  const { navigateAndResetCache } = useAuth()
  const exitMission = async () => navigateAndResetCache(PAM_V2_HOME_PATH, missionsKeys.all())

  return (
    <>
      <OfflineDialog />
      <MissionPageWrapper
        missionHeader={<MissionHeaderPam onClickClose={exitMission} missionId={missionId} />}
        missionGeneralInformations={<MissionGeneralInformationPam missionId={missionId} />}
        missionTimeLine={<MissionTimelinePam missionId={Number(missionId)} />}
        missionAction={<MissionActionPam missionId={Number(missionId)} actionId={actionId} />}
        missionFooter={<MissionPageFooter exitMission={exitMission} missionId={Number(missionId)} />}
      />
    </>
  )
}

export default MissionPamPage
