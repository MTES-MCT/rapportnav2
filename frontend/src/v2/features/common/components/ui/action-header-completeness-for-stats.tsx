import Text from '@common/components/ui/text'
import { useSelector } from '@tanstack/react-store'
import { createElement } from 'react'
import { Stack } from 'rsuite'
import { store } from '../../../../store'
import { useMissionCompletenessForStats } from '../../hooks/use-mission-completeness-for-stats.tsx'
import { NetworkSyncStatus } from '../../types/network-types.ts'
import { CompletenessForStats, CompletenessForStatsStatusEnum, MissionSourceEnum } from '../../types/mission-types.ts'

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
  const formIsValid = useSelector(store, state => state.formValidation.isValid)

  const effectiveCompleteness =
    formIsValid === false
      ? {
          status: CompletenessForStatsStatusEnum.INVALID,
          sources: completenessForStats?.sources ?? [MissionSourceEnum.RAPPORT_NAV]
        }
      : completenessForStats

  const { icon, statusMessage, color } = useMissionCompletenessForStats(effectiveCompleteness, isMissionFinished)

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
