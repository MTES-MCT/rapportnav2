import { useGlobalRoutes } from '@router/use-global-routes.tsx'
import React, { useRef } from 'react'
import { useLocation, useParams } from 'react-router'
import useAuth from '../features/auth/hooks/use-auth.tsx'
import PageWrapper from '../features/common/components/layout/page-wrapper.tsx'
import MissionPageFooter from '../features/common/components/ui/mission-page-footer.tsx'
import { missionsKeys } from '../features/common/services/query-keys.ts'
import { OwnerType } from '../features/common/types/owner-type.ts'
import MissionActionUlam from '../features/ulam/components/element/mission-action-ulam.tsx'
import MissionGeneralInformationUlam from '../features/ulam/components/element/mission-general-information-ulam.tsx'
import MissionHeaderUlam from '../features/ulam/components/element/mission-header-ulam.tsx'
import MissionTimelineUlam from '../features/ulam/components/element/mission-timeline-ulam.tsx'

const MissionUlamPage: React.FC = () => {
  const { getUrl } = useGlobalRoutes()
  let { missionId, actionId } = useParams()
  const { navigateAndResetCache } = useAuth()
  // Preserve the mission-list filters (carried as nav state on entry) so closing returns to the filtered list.
  // Captured once in a ref: this page stays mounted across in-mission action navigation, which drops the state.
  const { state } = useLocation()
  const listSearchRef = useRef<string>(state?.listSearch ?? '')
  const exitMission = async () =>
    navigateAndResetCache(`${getUrl(OwnerType.MISSION)}${listSearchRef.current}`, missionsKeys.all())

  return (
    <PageWrapper
      header={<MissionHeaderUlam onClickClose={exitMission} missionId={missionId} />}
      generalInformations={missionId ? <MissionGeneralInformationUlam missionId={missionId} /> : undefined}
      timeline={missionId ? <MissionTimelineUlam missionId={missionId} /> : undefined}
      action={<MissionActionUlam missionId={missionId} actionId={actionId} />}
      footer={<MissionPageFooter exitMission={exitMission} missionId={missionId} />}
    />
  )
}

export default MissionUlamPage
