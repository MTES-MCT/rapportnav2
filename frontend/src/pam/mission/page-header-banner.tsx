import React from 'react'
import { Banner, Level } from '@mtes-mct/monitor-ui'
import { CompletenessForStats, CompletenessForStatsStatusEnum } from '../../types/mission-types.ts'
import { MissionSourceEnum } from '../../types/env-mission-types.ts'

interface MissionPageHeaderBannerProps {
  completenessForStats?: CompletenessForStats
}

const message = (completenessForStats?: CompletenessForStats): string => {
  let message = ''
  if (completenessForStats?.status === CompletenessForStatsStatusEnum.COMPLETE) {
    message = `La mission est terminée et ses données sont complètes : le rapport est comptabilisé dans les statistiques. Vous pouvez désormais exporter le rapport de mission.`
  } else {
    message +=
      "La mission est terminée mais le rapport n'est pas complet : des données sont manquantes dans des champs à remplir "
    if (completenessForStats?.sources?.indexOf(MissionSourceEnum.RAPPORTNAV) !== -1) {
      message += 'par votre unité.'
    } else if (
      completenessForStats?.sources?.indexOf(MissionSourceEnum.MONITORFISH) !== -1 &&
      completenessForStats?.sources?.indexOf(MissionSourceEnum.MONITORENV) !== -1
    ) {
      message += 'par le CNSP et le CACEM.'
    } else if (completenessForStats?.sources?.indexOf(MissionSourceEnum.MONITORFISH) !== -1) {
      message += 'par le CNSP.'
    } else if (completenessForStats?.sources?.indexOf(MissionSourceEnum.MONITORENV) !== -1) {
      message += 'par le CACEM.'
    }
  }
  return message
}
const getBannerLevel = (completenessForStats?: CompletenessForStats): Level => {
  if (completenessForStats?.status === CompletenessForStatsStatusEnum.COMPLETE) {
    return Level.SUCCESS
  } else if (completenessForStats?.sources?.indexOf(MissionSourceEnum.RAPPORTNAV) !== -1) {
    return Level.ERROR
  } else {
    return Level.WARNING
  }
}

const MissionPageHeaderBanner: React.FC<MissionPageHeaderBannerProps> = ({ completenessForStats }) => {
  return (
    <Banner
      data-testid={'mission-report-status-banner'}
      isClosable={completenessForStats?.status === CompletenessForStatsStatusEnum.COMPLETE}
      isCollapsible={completenessForStats?.status !== CompletenessForStatsStatusEnum.COMPLETE}
      isHiddenByDefault={false}
      level={getBannerLevel(completenessForStats)}
      top={'60px'}
    >
      {message(completenessForStats)}
    </Banner>
  )
}

export default MissionPageHeaderBanner
