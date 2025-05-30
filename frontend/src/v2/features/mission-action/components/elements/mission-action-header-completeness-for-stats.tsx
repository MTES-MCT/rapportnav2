import Text from '@common/components/ui/text'
import { CompletenessForStats, MissionStatusEnum } from '@common/types/mission-types.ts'
import { createElement } from 'react'
import { Stack } from 'rsuite'
import { useMissionCompletenessForStats } from '../../../common/hooks/use-mission-completeness-for-stats'
import { NetworkSyncStatus } from '../../../common/types/network-types.ts'

interface MissionActionHeaderCompletenessForStatsProps {
  missionStatus?: MissionStatusEnum
  completenessForStats?: CompletenessForStats
  networkSyncStatus?: NetworkSyncStatus
}

export const MissionActionHeaderCompletenessForStats: React.FC<MissionActionHeaderCompletenessForStatsProps> = ({
  missionStatus,
  completenessForStats,
  networkSyncStatus
}) => {
  const { icon, statusMessage, color } = useMissionCompletenessForStats(
    completenessForStats,
    missionStatus,
    networkSyncStatus
  )

  return (
    <Stack spacing={'0.5rem'}>
      <Stack.Item alignSelf={'center'} style={{ color: color }}>
        {networkSyncStatus !== NetworkSyncStatus.UNSYNC && createElement(icon)}
      </Stack.Item>
      <Stack.Item alignSelf={'baseline'}>
        <Text as={'h3'} color={color}>
          {networkSyncStatus === NetworkSyncStatus.UNSYNC ? '' : statusMessage}
        </Text>
      </Stack.Item>
    </Stack>
  )
}

export default MissionActionHeaderCompletenessForStats
