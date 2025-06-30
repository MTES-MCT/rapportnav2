import Text from '@common/components/ui/text'
import { CompletenessForStats } from '@common/types/mission-types.ts'
import { createElement } from 'react'
import { Stack } from 'rsuite'
import { useMissionCompletenessForStats } from '../../../common/hooks/use-mission-completeness-for-stats'
import { NetworkSyncStatus } from '../../../common/types/network-types.ts'
import { useMissionFinished } from '../../../common/hooks/use-mission-finished.tsx'

interface MissionActionHeaderCompletenessForStatsProps {
  missionId: string
  completenessForStats?: CompletenessForStats
  networkSyncStatus?: NetworkSyncStatus
}

export const MissionActionHeaderCompletenessForStats: React.FC<MissionActionHeaderCompletenessForStatsProps> = ({
  missionId,
  completenessForStats,
  networkSyncStatus
}) => {
  const isMissionFinished = useMissionFinished(missionId)
  const { icon, statusMessage, color } = useMissionCompletenessForStats(completenessForStats, isMissionFinished)

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
