import { MissionSourceEnum } from '@common/types/env-mission-types'
import { CompletenessForStats, CompletenessForStatsStatusEnum, MissionStatusEnum } from '@common/types/mission-types'
import { Icon, IconProps, Level, THEME } from '@mtes-mct/monitor-ui'
import { FunctionComponent } from 'react'

const MISSION_INCOMPLETE_MESSAGE = `La mission est terminée mais le rapport n'est pas complet : des données sont manquantes dans des champs à remplir`
const MISSION_COMPLETED_MESSAGE = `La mission est terminée et ses données sont complètes : le rapport est comptabilisé dans les statistiques. Vous pouvez désormais exporter le rapport de mission.`

type Component = {
  text?: string
  color: string
  icon: FunctionComponent<IconProps>
}

interface MissionCompletenessTagHook {
  getCompletenessForStats: () => Component
  getBannerLevel: (completenessForStats?: CompletenessForStats) => Level
  getBannerMessage: (completenessForStats?: CompletenessForStats) => string
  getCompletenessForStatsStatus: () => Component
}

export function useMissionCompletenessForStats(
  missionStatus?: MissionStatusEnum,
  completenessForStatsType?: CompletenessForStatsStatusEnum
): MissionCompletenessTagHook {
  const getCompletenessForStats = (): Component => {
    const stat: Component = {} as Component
    if (completenessForStatsType && missionStatus === MissionStatusEnum.ENDED) {
      if (completenessForStatsType === CompletenessForStatsStatusEnum.COMPLETE) {
        stat.text = 'Complété'
        stat.icon = Icon.Confirm
        stat.color = THEME.color.mediumSeaGreen
      } else {
        stat.text = 'À compléter'
        stat.icon = Icon.AttentionFilled
        stat.color = THEME.color.maximumRed
      }
    } else {
      if (completenessForStatsType === CompletenessForStatsStatusEnum.COMPLETE) {
        stat.icon = Icon.Confirm
        stat.text = 'Données à jour'
        stat.color = THEME.color.mediumSeaGreen
      } else {
        stat.text = 'À compléter'
        stat.icon = Icon.AttentionFilled
        stat.color = THEME.color.charcoal
      }
    }
    return stat
  }

  const fromCnspAndCacem = (stats?: CompletenessForStats) =>
    stats?.sources?.indexOf(MissionSourceEnum.MONITORFISH) !== -1 &&
    stats?.sources?.indexOf(MissionSourceEnum.MONITORENV) !== -1

  const fromCnsp = (stats?: CompletenessForStats) => stats?.sources?.indexOf(MissionSourceEnum.MONITORFISH) !== -1
  const fromCacem = (stats?: CompletenessForStats) => stats?.sources?.indexOf(MissionSourceEnum.MONITORENV) !== -1

  const getBannerMessage = (completenessForStats?: CompletenessForStats): string => {
    if (completenessForStats?.status === CompletenessForStatsStatusEnum.COMPLETE) {
      return MISSION_COMPLETED_MESSAGE
    }
    let by = `votre unité`
    if (fromCnspAndCacem(completenessForStats)) {
      by = `le CNSP et le CACEM`
    } else if (fromCnsp(completenessForStats)) {
      by = `le CNSP`
    } else if (fromCacem(completenessForStats)) {
      by = `le CACEM`
    }
    return `${MISSION_INCOMPLETE_MESSAGE} par ${by}.`
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

  const getColorReportStatus = (isMissionFinished?: boolean, isCompleteForStats?: boolean) => {
    if (isMissionFinished) {
      return isCompleteForStats ? THEME.color.mediumSeaGreen : THEME.color.maximumRed
    }
    return THEME.color.charcoal
  }
  const getIconReportStatus = (isCompleteForStats?: boolean) =>
    isCompleteForStats ? Icon.Confirm : Icon.AttentionFilled

  const getCompletenessForStatsStatus = (): Component => {
    const isMissionFinished = missionStatus === MissionStatusEnum.ENDED
    const isCompleteForStats = completenessForStatsType === CompletenessForStatsStatusEnum.COMPLETE
    return {
      icon: getIconReportStatus(isMissionFinished),
      color: getColorReportStatus(isMissionFinished, isCompleteForStats)
    }
  }

  return { getCompletenessForStatsStatus, getBannerLevel, getBannerMessage, getCompletenessForStats }
}
