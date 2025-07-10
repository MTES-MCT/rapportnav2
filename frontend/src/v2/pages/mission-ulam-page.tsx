import { ULAM_V2_HOME_PATH } from '@router/routes.tsx'
import React from 'react'
import { useParams } from 'react-router-dom'
import useAuth from '../features/auth/hooks/use-auth.tsx'
import MissionPageWrapper from '../features/common/components/layout/mission-page-wrapper.tsx'
import MissionPageFooter from '../features/common/components/ui/mission-page-footer.tsx'
import { missionsKeys } from '../features/common/services/query-keys.ts'
import MissionActionUlam from '../features/ulam/components/element/mission-action-ulam.tsx'
import MissionGeneralInformationUlam from '../features/ulam/components/element/mission-general-information-ulam.tsx'
import MissionHeaderUlam from '../features/ulam/components/element/mission-header-ulam.tsx'
import MissionTimelineUlam from '../features/ulam/components/element/mission-timeline-ulam.tsx'

const MissionUlamPage: React.FC = () => {
  let { missionId, actionId } = useParams()
  const { navigateAndResetCache } = useAuth()
  const exitMission = async () => navigateAndResetCache(ULAM_V2_HOME_PATH, missionsKeys.all())

  return (
    <MissionPageWrapper
      missionHeader={<MissionHeaderUlam onClickClose={exitMission} missionId={missionId} />}
      missionGeneralInformations={missionId ? <MissionGeneralInformationUlam missionId={missionId} /> : undefined}
      missionTimeLine={missionId ? <MissionTimelineUlam missionId={missionId} /> : undefined}
      missionAction={<MissionActionUlam missionId={missionId} actionId={actionId} />}
      missionFooter={<MissionPageFooter exitMission={exitMission} missionId={Number(missionId)} type={'ULAM'} />}
    />
  )
}

export default MissionUlamPage
