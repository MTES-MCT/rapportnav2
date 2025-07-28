import Text from '@common/components/ui/text'
import { CompletenessForStats } from '@common/types/mission-types.ts'
import { createElement } from 'react'
import { Stack } from 'rsuite'
import { useMissionCompletenessForStats } from '../../hooks/use-mission-completeness-for-stats.tsx'
import { NetworkSyncStatus } from '../../types/network-types.ts'

interface ActionHeaderCompletenessForStatsProps {
  isMissionFinished?: boolean
  completenessForStats?: CompletenessForStats
  networkSyncStatus?: NetworkSyncStatus
}

export const ActionHeaderCompletenessForStats: React.FC<ActionHeaderCompletenessForStatsProps> = ({
  isMissionFinished,
  completenessForStats,
  networkSyncStatus
}) => {
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

export default ActionHeaderCompletenessForStats
