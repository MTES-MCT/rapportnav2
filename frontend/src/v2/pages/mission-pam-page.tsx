import { useGlobalRoutes } from '@router/use-global-routes.tsx'
import React from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import PageWrapper from '../features/common/components/layout/page-wrapper.tsx'
import MissionPageFooter from '../features/common/components/ui/mission-page-footer.tsx'
import { OwnerType } from '../features/common/types/owner-type.ts'
import MissionGeneralInformationPam from '../features/pam/components/element/general-info/mission-general-information-pam.tsx'
import MissionActionPam from '../features/pam/components/element/mission-action-pam.tsx'
import MissionHeaderPam from '../features/pam/components/element/mission-header-pam.tsx'
import MissionTimelinePam from '../features/pam/components/element/mission-timeline-pam.tsx'
import OfflineDialog from '../features/pam/components/ui/offline-dialog.tsx'

const MissionPamPage: React.FC = () => {
  const { getUrl } = useGlobalRoutes()
  let { missionId, actionId } = useParams()
  const navigate = useNavigate()
  const exitMission = async () => navigate(getUrl(OwnerType.MISSION))

  return (
    <>
      <OfflineDialog />
      <PageWrapper
        header={<MissionHeaderPam onClickClose={exitMission} missionId={missionId} />}
        generalInformations={<MissionGeneralInformationPam missionId={missionId} />}
        timeline={missionId ? <MissionTimelinePam missionId={missionId} /> : undefined}
        action={missionId ? <MissionActionPam missionId={missionId} actionId={actionId} /> : undefined}
        footer={<MissionPageFooter exitMission={exitMission} missionId={missionId} type={'PAM'} />}
      />
    </>
  )
}

export default MissionPamPage
