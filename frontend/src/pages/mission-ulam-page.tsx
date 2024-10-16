import { formatTime } from '@common/utils/dates-for-humans.ts'
import useAuth from '@features/auth/hooks/use-auth.tsx'
import MissionGeneralInformationUlam from '@features/ulam/components/element/mission-general-information-ulam.tsx'
import MissionTimelineUlam from '@features/ulam/components/element/mission-timeline-ulam.tsx'
import MissionActionHeader from '@features/v2/common/components/elements/mission-action-header.tsx'
import MissionTimelineHeader from '@features/v2/common/components/elements/mission-timeline-Header.tsx'
import MissionPageFooterWrapper from '@features/v2/common/components/layout/mission-page-footer-wrapper.tsx'
import MissionPageHeaderWrapper from '@features/v2/common/components/layout/mission-page-header-wrapper.tsx'
import MissionPageSectionWrapper from '@features/v2/common/components/layout/mission-page-section-wrapper.tsx'
import MissionPageWrapper from '@features/v2/common/components/layout/mission-page-wrapper.tsx'
import MissionGeneralInformationHeader from '@features/v2/common/components/ui/mission-general-information-header.tsx'
import MissionPageError from '@features/v2/common/components/ui/mission-page-error.tsx'
import MissionPageLoading from '@features/v2/common/components/ui/mission-page-loading.tsx'
import { useMissionExport } from '@features/v2/common/hooks/use-mission-export.tsx'
import { useMissionExcerptQuery } from '@features/v2/common/services/use-mission-excerpt.tsx'
import { ModuleType } from '@features/v2/common/types/module-type.ts'
import { ULAM_HOME_PATH } from '@router/router.tsx'
import React from 'react'
import { useParams } from 'react-router-dom'
import useApolloLastSync from '../features/common/hooks/use-apollo-last-sync.tsx'

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
          sectionHeader={<MissionTimelineHeader missionId={missionId} moduleType={ModuleType.ULAM} hideStatus={true} />}
          sectionBody={<MissionTimelineUlam missionId={missionId} />}
        />
      }
      missionAction={
        <MissionPageSectionWrapper
          //hide={!actionId}
          sectionHeader={<MissionActionHeader missionId={missionId} actionId={actionId} moduleType={ModuleType.ULAM} />}
          sectionBody={<>Body</>}
        />
      }
      missionFooter={<MissionPageFooterWrapper lastSyncText={lastSyncText} exitMission={exitMission} />}
    />
  )
}

export default MissionUlamPage
