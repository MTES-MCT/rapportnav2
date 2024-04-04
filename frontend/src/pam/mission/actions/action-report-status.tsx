import { FC } from 'react'
import { ExclamationPoint, Icon, THEME } from '@mtes-mct/monitor-ui'
import { MissionSourceEnum } from '../../../types/env-mission-types'
import { MissionStatusEnum } from '../../../types/mission-types.ts'
import { Stack } from 'rsuite'
import Text from '../../../ui/text.tsx'

export interface ActionReportStatusProps {
  missionStatus: MissionStatusEnum
  actionSource: MissionSourceEnum
  dataIsComplete?: boolean
}

const colorForReportStatus = (missionStatus: MissionStatusEnum, dataIsComplete?: boolean) => {
  if (dataIsComplete) {
    return THEME.color.mediumSeaGreen
  }
  if (missionStatus === MissionStatusEnum.ENDED) {
    return THEME.color.maximumRed
  }
  return THEME.color.charcoal
}

const messageForReportStatus = (actionSource: MissionSourceEnum, dataIsComplete?: boolean) => {
  if (dataIsComplete) {
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

const ActionReportStatus: FC<ActionReportStatusProps> = ({ missionStatus, actionSource, dataIsComplete }) => {
  const AppropriateIcon = dataIsComplete
    ? () => <Icon.Confirm color={colorForReportStatus(missionStatus, dataIsComplete)} data-testid={'report-complete'} />
    : () => (
        <ExclamationPoint
          backgroundColor={colorForReportStatus(missionStatus, dataIsComplete)}
          data-testid={'report-incomplete'}
        />
      )

  return (
    <Stack spacing={'0.5rem'}>
      <Stack.Item alignSelf={'center'}>
        <AppropriateIcon />
      </Stack.Item>
      <Stack.Item alignSelf={'center'}>
        <Text as={'h3'} color={colorForReportStatus(missionStatus, dataIsComplete)}>
          {messageForReportStatus(actionSource, dataIsComplete)}
        </Text>
      </Stack.Item>
    </Stack>
  )
}

export default ActionReportStatus
