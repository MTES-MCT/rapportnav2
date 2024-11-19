import { formatTime } from '@common/utils/dates-for-humans.ts'

import { ULAM_V2_HOME_PATH } from '@router/router.tsx'
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
import { useMissionReportExport } from '../features/common/hooks/use-mission-report-export.tsx'
import { useMissionExcerptQuery } from '../features/common/services/use-mission-excerpt.tsx'
import MissionActionHeader from '../features/mission-action/components/elements/mission-action-header.tsx'
import MissionActionUlam from '../features/ulam/components/element/mission-action-ulam.tsx'
import MissionGeneralInformationUlam from '../features/ulam/components/element/mission-general-information-ulam.tsx'
import MissionTimelineHeaderUlam from '../features/ulam/components/element/mission-timeline-header-ulam.tsx'
import MissionTimelineUlam from '../features/ulam/components/element/mission-timeline-ulam.tsx'

const MissionUlamPage: React.FC = () => {
  const lastSync = useApolloLastSync()
  let { missionId, actionId } = useParams()
  const { navigateAndResetCache } = useAuth()
  const exitMission = async () => navigateAndResetCache(ULAM_V2_HOME_PATH)

  const { exportMission, exportIsLoading } = useMissionReportExport(missionId)
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
          sectionHeader={<MissionTimelineHeaderUlam missionId={Number(missionId)} />}
          sectionBody={<MissionTimelineUlam missionId={Number(missionId)} />}
        />
      }
      missionAction={
        <MissionPageSectionWrapper
          hide={!actionId}
          sectionHeader={
            <MissionActionHeader missionId={Number(missionId)} actionId={actionId} missionStatus={mission?.status} />
          }
          sectionBody={<MissionActionUlam missionId={Number(missionId)} actionId={actionId} />}
        />
      }
      missionFooter={<MissionPageFooterWrapper lastSyncText={lastSyncText} exitMission={exitMission} />}
    />
  )
}

export default MissionUlamPage
