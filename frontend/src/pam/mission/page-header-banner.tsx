import React from 'react'
import { Banner, Level } from '@mtes-mct/monitor-ui'
import { MissionReportStatus, MissionReportStatusEnum } from '../../types/mission-types.ts'
import { MissionSourceEnum } from '../../types/env-mission-types.ts'

interface MissionPageHeaderBannerProps {
  reportStatus?: MissionReportStatus
}

const message = (reportStatus?: MissionReportStatus): string => {
  let message = ''
  if (reportStatus?.status === MissionReportStatusEnum.COMPLETE) {
    message = `La mission est terminée et ses données sont complètes : le rapport est comptabilisé dans les statistiques. Vous pouvez désormais exporter le rapport de mission.`
  } else {
    message +=
      "La mission est terminée mais le rapport n'est pas complet : des données sont manquantes dans des champs à remplir "
    if (reportStatus?.sources?.indexOf(MissionSourceEnum.RAPPORTNAV) !== -1) {
      message += 'par votre unité.'
    } else if (reportStatus?.sources?.indexOf(MissionSourceEnum.MONITORFISH) !== -1) {
      message += 'par le CNSP.'
    } else if (reportStatus?.sources?.indexOf(MissionSourceEnum.MONITORENV) !== -1) {
      message += 'par le CACEM.'
    }
  }
  return message
}
const getBannerLevel = (reportStatus?: MissionReportStatus): Level => {
  if (reportStatus?.status === MissionReportStatusEnum.COMPLETE) {
    return Level.SUCCESS
  } else if (reportStatus?.sources?.indexOf(MissionSourceEnum.RAPPORTNAV) !== -1) {
    return Level.ERROR
  } else {
    return Level.WARNING
  }
}

const MissionPageHeaderBanner: React.FC<MissionPageHeaderBannerProps> = ({ reportStatus }) => {
  return (
    <Banner
      data-testid={'mission-report-status-banner'}
      isClosable={reportStatus?.status === MissionReportStatusEnum.COMPLETE}
      isCollapsible={reportStatus?.status !== MissionReportStatusEnum.COMPLETE}
      isHiddenByDefault={false}
      level={getBannerLevel(reportStatus)}
      top={'60px'}
    >
      {message(reportStatus)}
    </Banner>
  )
}

export default MissionPageHeaderBanner
