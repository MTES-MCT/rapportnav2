import { FC } from 'react'
import MissionCompletenessForStatsTag from '../../../common/components/elements/mission-completeness-for-stats-tag'
import PageHeaderWrapper from '../../../common/components/layout/page-header-wrapper'
import MissionPageHeaderBanner from '../../../common/components/ui/mission-page-header-banner'
import MissionPageHeaderTimeConversion from '../../../common/components/ui/mission-page-header-time-conversion'
import MissionSourceTag from '../../../common/components/ui/mission-source-tag'
import MissionStatusTag from '../../../common/components/ui/mission-status-tag'
import { useDate } from '../../../common/hooks/use-date'
import useGetMissionQuery from '../../../common/services/use-mission'
import { MissionStatusEnum } from '../../../common/types/mission-types'

interface MissionHeaderProps {
  missionId?: string
  onClickClose: () => void
}

const MissionHeaderPam: FC<MissionHeaderProps> = ({ missionId, onClickClose }) => {
  const { formatMissionName } = useDate()
  const { data: mission } = useGetMissionQuery(missionId)
  return (
    <PageHeaderWrapper
      onClickClose={onClickClose}
      date={<>{formatMissionName(mission?.data?.startDateTimeUtc)}</>}
      tags={
        <>
          <MissionSourceTag missionSource={mission?.data?.missionSource} />
          <MissionStatusTag status={mission?.status} />
          <MissionCompletenessForStatsTag
            missionStatus={mission?.status}
            completenessForStats={mission?.completenessForStats}
          />
        </>
      }
      utcTime={<MissionPageHeaderTimeConversion />}
      banner={
        <>
          {mission?.status === MissionStatusEnum.ENDED && (
            <MissionPageHeaderBanner completenessForStats={mission?.completenessForStats} />
          )}
        </>
      }
    />
  )
}

export default MissionHeaderPam
