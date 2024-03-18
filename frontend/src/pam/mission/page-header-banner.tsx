import React from 'react'
import { Stack } from 'rsuite'
import { THEME } from '@mtes-mct/monitor-ui'
import Text from '../../ui/text'
import { MissionReportStatus, MissionReportStatusEnum } from '../../types/mission-types.ts'
import { MissionSourceEnum } from '../../types/env-mission-types.ts'

interface MissionPageHeaderBannerProps {
  reportStatus?: MissionReportStatus
}

const message = (reportStatus?: MissionReportStatus): string => {
  let message = ''
  if (reportStatus?.status === MissionReportStatusEnum.COMPLETE) {
    message = `La mission est terminée et ses données sont complètes : le rapport est comptabilisé dans les statistiques. Vous pouvez désormais <u>exporter le rapport de mission</u>.`
  } else {
    message +=
      "La mission est terminée mais le rapport n'est pas complet : des données sont manquantes dans des champs à remplir "
    if (reportStatus?.source?.indexOf(MissionSourceEnum.RAPPORTNAV) != -1) {
      message += 'par votre unité.'
    } else if (reportStatus?.source?.indexOf(MissionSourceEnum.MONITORFISH) != -1) {
      message += 'par le CNSP.'
    } else if (reportStatus?.source?.indexOf(MissionSourceEnum.MONITORENV) != -1) {
      message += 'par le CACEM.'
    }
  }
  return message
}
const colorPalette = (reportStatus?: MissionReportStatus): { color: string; backgroundColor: string } | undefined => {
  let colors = undefined
  if (reportStatus?.status === MissionReportStatusEnum.COMPLETE) {
    colors = {
      color: THEME.color.mediumSeaGreen,
      backgroundColor: THEME.color.mediumSeaGreen25
    }
  } else if (reportStatus?.source?.indexOf(MissionSourceEnum.RAPPORTNAV) != -1) {
    colors = {
      color: THEME.color.maximumRed,
      backgroundColor: THEME.color.maximumRed15
    }
  } else {
    colors = {
      color: THEME.color.charcoal,
      backgroundColor: THEME.color.goldenPoppy25
    }
  }
  return colors
}

const MissionPageHeaderBanner: React.FC<MissionPageHeaderBannerProps> = ({ reportStatus }) => {
  const colors = colorPalette(reportStatus)
  return (
    <Stack
      data-testid={'mission-page-header-banner'}
      direction={'row'}
      justifyContent={'space-between'}
      alignItems={'center'}
      style={{
        position: 'absolute',
        backgroundColor: colors?.backgroundColor,
        width: '100%',
        top: '60px',
        zIndex: 100000000,
        minHeight: '50px',
        borderBottom: `4px solid ${colors?.color}`,
        padding: '0 2rem'
      }}
    >
      <Stack.Item grow={2} alignSelf={'center'}>
        <Text as={'h2'} color={colors?.color} weight={'medium'} style={{ textAlign: 'center' }}>
          {message(reportStatus)}
        </Text>
      </Stack.Item>
      <Stack.Item alignSelf={'center'}>
        <Text
          as={'h2'}
          color={colors?.color}
          weight={'normal'}
          fontStyle={'italic'}
          decoration={'underline'}
          style={{ textAlign: 'center' }}
        >
          Masquer
        </Text>
      </Stack.Item>
    </Stack>
  )
}

export default MissionPageHeaderBanner
