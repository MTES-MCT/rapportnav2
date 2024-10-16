import { formatTime } from '@common/utils/dates-for-humans.ts'

import { ULAM_HOME_PATH } from '@router/router.tsx'
import React from 'react'
import { useParams } from 'react-router-dom'
import useApolloLastSync from '../../features/common/hooks/use-apollo-last-sync.tsx'
import useAuth from '../features/auth/hooks/use-auth.tsx'
import MissionPageFooterWrapper from '../features/common/components/layout/mission-page-footer-wrapper.tsx'
import MissionPageHeaderWrapper from '../features/common/components/layout/mission-page-header-wrapper.tsx'
import MissionPageSectionWrapper from '../features/common/components/layout/mission-page-section-wrapper.tsx'
import MissionPageWrapper from '../features/common/components/layout/mission-page-wrapper.tsx'
import MissionGeneralInformationHeader from '../features/common/components/ui/mission-general-information-header.tsx'
import MissionPageError from '../features/common/components/ui/mission-page-error.tsx'
import MissionPageLoading from '../features/common/components/ui/mission-page-loading.tsx'
import { useMissionExport } from '../features/common/hooks/use-mission-export.tsx'
import { useMissionExcerptQuery } from '../features/common/services/use-mission-excerpt.tsx'
import { ModuleType } from '../features/common/types/module-type.ts'
import MissionActionHeader from '../features/mission-action/components/elements/mission-action-header.tsx'
import MissionGeneralInformationUlam from '../features/ulam/components/element/mission-general-information-ulam.tsx'
import MissionTimelineHeaderUlam from '../features/ulam/components/element/mission-timeline-header-ulam.tsx'
import MissionTimelineUlam from '../features/ulam/components/element/mission-timeline-ulam.tsx'

const MissionUlamPage: React.FC = () => {
  const lastSync = useApolloLastSync()
  let { missionId, actionId } = useParams()
  const { navigateAndResetCache } = useAuth()
  const exitMission = async () => navigateAndResetCache(ULAM_HOME_PATH)

  const { exportMission, exportIsLoading } = useMissionExport(missionId)
  const { loading, error, data: mission } = useMissionExcerptQuery(missionId)

  const lastSyncText = lastSync ? formatTime(new Date(parseInt(lastSync!!, 10))) : undefined

  if (error) return <MissionPageError />
  if (loading) return <MissionPageLoading />

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
          sectionBody={<MissionGeneralInformationUlam />}
        />
      }
      missionTimeLine={
        <MissionPageSectionWrapper
          sectionHeader={<MissionTimelineHeaderUlam missionId={missionId} />}
          sectionBody={<MissionTimelineUlam missionId={missionId} />}
        />
      }
      missionAction={
        <MissionPageSectionWrapper
          hide={!actionId}
          sectionHeader={<MissionActionHeader missionId={missionId} actionId={actionId} moduleType={ModuleType.ULAM} />}
          sectionBody={<>Body</>}
        />
      }
      missionFooter={<MissionPageFooterWrapper lastSyncText={lastSyncText} exitMission={exitMission} />}
    />
  )
}

export default MissionUlamPage
