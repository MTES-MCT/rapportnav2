import { FC } from 'react'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { MissionSourceEnum } from '../../../types/env-mission-types'
import { Stack } from 'rsuite'
import Text from '../../../ui/text.tsx'

export interface ActionReportStatusProps {
  actionSource: MissionSourceEnum
  isCompleteForStats?: boolean
  isMissionFinished?: boolean
}

const colorForReportStatus = (isMissionFinished?: boolean, isCompleteForStats?: boolean) => {
  if (isMissionFinished) {
    return isCompleteForStats ? THEME.color.mediumSeaGreen : THEME.color.maximumRed
  }
  return THEME.color.charcoal
}

const messageForReportStatus = (actionSource: MissionSourceEnum, isCompleteForStats?: boolean) => {
  if (isCompleteForStats) {
    return 'Les champs indispensables aux statistiques sont remplis.'
  }

  let sourceName
  switch (actionSource) {
    case MissionSourceEnum.RAPPORTNAV:
      sourceName = "l'unité"
      break
    case MissionSourceEnum.MONITORENV:
      sourceName = 'le CACEM'
      break
    default:
      sourceName = 'le CNSP'
  }

  return `Des champs indispensables sont à remplir par ${sourceName}.`
}

const ActionReportStatus: FC<ActionReportStatusProps> = ({ actionSource, isMissionFinished, isCompleteForStats }) => {
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
      <Stack.Item alignSelf={'center'}>
        <Text as={'h3'} color={colorForReportStatus(isMissionFinished, isCompleteForStats)}>
          {messageForReportStatus(actionSource, isCompleteForStats)}
        </Text>
      </Stack.Item>
    </Stack>
  )
}

export default ActionReportStatus
