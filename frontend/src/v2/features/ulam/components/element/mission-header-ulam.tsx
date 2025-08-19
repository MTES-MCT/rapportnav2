import { FC, useEffect } from 'react'
import { setMissionStatus } from '../../../../store/slices/mission-reducer'
import MissionCompletenessForStatsTag from '../../../common/components/elements/mission-completeness-for-stats-tag'
import PageHeaderWrapper from '../../../common/components/layout/page-header-wrapper'
import MissionPageHeaderBanner from '../../../common/components/ui/mission-page-header-banner'
import MissionSourceTag from '../../../common/components/ui/mission-source-tag'
import MissionStatusTag from '../../../common/components/ui/mission-status-tag'
import TimeConversion from '../../../common/components/ui/time-conversion'
import { useDate } from '../../../common/hooks/use-date'
import useGetMissionQuery from '../../../common/services/use-mission'
import { MissionStatusEnum } from '../../../common/types/mission-types'

interface MissionHeaderProps {
  missionId?: string
  onClickClose: () => void
}

const MissionHeaderUlam: FC<MissionHeaderProps> = ({ missionId, onClickClose }) => {
  const { formatMissionName } = useDate()
  const { data: mission } = useGetMissionQuery(missionId)

  useEffect(() => {
    setMissionStatus(mission?.status)
  }, [mission])

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
      utcTime={<TimeConversion />}
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

export default MissionHeaderUlam
