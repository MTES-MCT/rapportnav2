import { FC } from 'react'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { MissionSourceEnum } from '@common/types/env-mission-types.ts'
import { Stack } from 'rsuite'
import Text from '../../../../../common/components/ui/text.tsx'
import { CompletenessForStats, CompletenessForStatsStatusEnum } from '@common/types/mission-types.ts'

export interface ActionCompletenessForStatsMessageProps {
  completenessForStats?: CompletenessForStats
  isMissionFinished?: boolean
}

const colorForReportStatus = (isMissionFinished?: boolean, isCompleteForStats?: boolean) => {
  if (isMissionFinished) {
    return isCompleteForStats ? THEME.color.mediumSeaGreen : THEME.color.maximumRed
  }
  return THEME.color.charcoal
}

const messageForReportStatus = (completenessForStats?: CompletenessForStats) => {
  if (completenessForStats?.status === CompletenessForStatsStatusEnum.COMPLETE) {
    return 'Les champs indispensables aux statistiques sont remplis.'
  }

  let sourceName = ''
  if (completenessForStats?.sources?.indexOf(MissionSourceEnum.RAPPORTNAV) !== -1) {
    sourceName = "par l'unité"
  } else if (completenessForStats?.sources?.indexOf(MissionSourceEnum.MONITORENV) !== -1) {
    sourceName = 'par le CACEM'
  } else if (completenessForStats?.sources?.indexOf(MissionSourceEnum.MONITORFISH) !== -1) {
    sourceName = 'par le CNSP'
  }
  return `Des champs indispensables sont à remplir ${sourceName}.`
}

const ActionCompletenessForStatsMessage: FC<ActionCompletenessForStatsMessageProps> = ({
  isMissionFinished,
  completenessForStats
}) => {
  const isCompleteForStats = completenessForStats?.status === CompletenessForStatsStatusEnum.COMPLETE
  const AppropriateIcon = isCompleteForStats
    ? () => (
        <Icon.Confirm
          color={colorForReportStatus(isMissionFinished, isCompleteForStats)}
          data-testid={'report-complete'}
        />
      )
    : () => (
        <Icon.AttentionFilled
          color={colorForReportStatus(isMissionFinished, isCompleteForStats)}
          data-testid={'report-incomplete'}
        />
      )

  return (
    <Stack spacing={'0.5rem'}>
      <Stack.Item alignSelf={'center'}>
        <AppropriateIcon />
      </Stack.Item>
      <Stack.Item alignSelf={'baseline'}>
        <Text as={'h3'} color={colorForReportStatus(isMissionFinished, isCompleteForStats)}>
          {messageForReportStatus(completenessForStats)}
        </Text>
      </Stack.Item>
    </Stack>
  )
}

export default ActionCompletenessForStatsMessage
