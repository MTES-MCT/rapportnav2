import { ULAM_V2_HOME_PATH } from '@router/routes.tsx'
import React from 'react'
import { useParams } from 'react-router-dom'
import useAuth from '../features/auth/hooks/use-auth.tsx'
import MissionPageWrapper from '../features/common/components/layout/mission-page-wrapper.tsx'
import MissionPageFooter from '../features/common/components/ui/mission-page-footer.tsx'
import { useMissionReportExport } from '../features/common/hooks/use-mission-report-export.tsx'
import { QueryKeyType } from '../features/common/types/query-key-type.ts'
import MissionActionUlam from '../features/ulam/components/element/mission-action-ulam.tsx'
import MissionGeneralInformationUlam from '../features/ulam/components/element/mission-general-information-ulam.tsx'
import MissionHeaderUlam from '../features/ulam/components/element/mission-header-ulam.tsx'
import MissionTimelineUlam from '../features/ulam/components/element/mission-timeline-ulam.tsx'

const MissionUlamPage: React.FC = () => {
  let { missionId, actionId } = useParams()
  const { navigateAndResetCache } = useAuth()
  const { exportMission, exportIsLoading } = useMissionReportExport() //TODO: Corriger cet export et la fonction
  const exitMission = async () => navigateAndResetCache(ULAM_V2_HOME_PATH, [QueryKeyType.MISSION_LIST])

  return (
    <MissionPageWrapper
      missionHeader={
        <MissionHeaderUlam
          onClickClose={exitMission}
          onClickExport={exportMission}
          exportLoading={exportIsLoading}
          missionId={Number(missionId)}
        />
      }
      missionGeneralInformations={<MissionGeneralInformationUlam missionId={Number(missionId)} />}
      missionTimeLine={<MissionTimelineUlam missionId={Number(missionId)} />}
      missionAction={<MissionActionUlam missionId={Number(missionId)} actionId={actionId} />}
      missionFooter={<MissionPageFooter exitMission={exitMission} />}
    />
  )
}

export default MissionUlamPage
