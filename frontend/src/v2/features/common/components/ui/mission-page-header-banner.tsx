import { CompletenessForStats, CompletenessForStatsStatusEnum } from '@common/types/mission-types'
import { Banner } from '@mtes-mct/monitor-ui'
import React from 'react'
import { useMissionCompletenessForStats } from '../../hooks/use-mission-completeness-for-stats'

interface MissionPageHeaderBannerProps {
  completenessForStats?: CompletenessForStats
}

const MissionPageHeaderBanner: React.FC<MissionPageHeaderBannerProps> = ({ completenessForStats }) => {
  const { bannerLevel, bannerMessage } = useMissionCompletenessForStats(completenessForStats)
  return (
    <Banner
      data-testid={'mission-report-status-banner'}
      isClosable={completenessForStats?.status === CompletenessForStatsStatusEnum.COMPLETE}
      isCollapsible={completenessForStats?.status !== CompletenessForStatsStatusEnum.COMPLETE}
      isHiddenByDefault={false}
      level={bannerLevel}
      top={'60px'}
    >
      {bannerMessage}
    </Banner>
  )
}

export default MissionPageHeaderBanner
