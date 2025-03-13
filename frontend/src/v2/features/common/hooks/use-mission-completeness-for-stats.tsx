import { MissionSourceEnum } from '@common/types/env-mission-types'
import { CompletenessForStats, CompletenessForStatsStatusEnum, MissionStatusEnum } from '@common/types/mission-types'
import { Icon, IconProps, Level, THEME } from '@mtes-mct/monitor-ui'
import { FunctionComponent } from 'react'

enum MissionStateType {
  MISSION_ENDED_AND_COMPLETE = 'MISSION_ENDED_AND_COMPLETE',
  MISSION_ENDED_AND_NOT_COMPLETE = 'MISSION_ENDED_AND_NOT_COMPLETE',
  MISSION_NOT_ENDED_AND_COMPLETE = 'MISSION_IN_PROGRESS_AND_COMPLETE',
  MISSION_NOT_ENDED_AND_NOT_COMPLETE = 'MISSION_IN_PROGRESS_AND_NOT_COMPLETE'
}

type Component = {
  text?: string
  color: string
  icon: FunctionComponent<IconProps>
}

type ComponentStateType = { [key in MissionStateType]: Component }

const MISSION_STATE_REGISTRY: ComponentStateType = {
  [MissionStateType.MISSION_ENDED_AND_COMPLETE]: {
    text: 'Complété',
    icon: Icon.Confirm,
    color: THEME.color.mediumSeaGreen
  },
  [MissionStateType.MISSION_ENDED_AND_NOT_COMPLETE]: {
    text: 'À compléter',
    icon: Icon.AttentionFilled,
    color: THEME.color.maximumRed
  },
  [MissionStateType.MISSION_NOT_ENDED_AND_COMPLETE]: {
    text: 'Données à jour',
    icon: Icon.Confirm,
    color: THEME.color.mediumSeaGreen
  },
  [MissionStateType.MISSION_NOT_ENDED_AND_NOT_COMPLETE]: {
    text: 'À compléter',
    icon: Icon.AttentionFilled,
    color: THEME.color.charcoal
  }
}

const MISSION_INCOMPLETE_MESSAGE = `La mission est terminée mais le rapport n'est pas complet : des données sont manquantes dans des champs à remplir`
const MISSION_COMPLETED_MESSAGE = `La mission est terminée et ses données sont complètes : le rapport est comptabilisé dans les statistiques. Vous pouvez désormais exporter le rapport de mission.`

type MissionCompletenessTagHook = {
  bannerLevel: Level
  bannerMessage: string
  statusMessage: string
  isCompleteForStats: (completenessForStats?: CompletenessForStats) => boolean
} & Component

export function useMissionCompletenessForStats(
  completenessForStats?: CompletenessForStats,
  missionStatus?: MissionStatusEnum
): MissionCompletenessTagHook {
  const isCompleteForStats = (completenessForStats?: CompletenessForStats) =>
    completenessForStats?.status === CompletenessForStatsStatusEnum.COMPLETE

  const isMissionEnded = (status?: MissionStatusEnum) => status === MissionStatusEnum.ENDED

  const getMissionStateType = () => {
    if (isMissionEnded(missionStatus) && isCompleteForStats(completenessForStats)) {
      return MissionStateType.MISSION_ENDED_AND_COMPLETE
    }
    if (isMissionEnded(missionStatus) && !isCompleteForStats(completenessForStats)) {
      return MissionStateType.MISSION_ENDED_AND_NOT_COMPLETE
    }
    if (!isMissionEnded(missionStatus) && isCompleteForStats(completenessForStats)) {
      return MissionStateType.MISSION_NOT_ENDED_AND_COMPLETE
    }
    if (!isMissionEnded(missionStatus) && !isCompleteForStats(completenessForStats)) {
      return MissionStateType.MISSION_NOT_ENDED_AND_NOT_COMPLETE
    }
  }

  const getCompletenessForStats = (): Component => {
    const missionStateType = getMissionStateType()
    return missionStateType ? MISSION_STATE_REGISTRY[missionStateType] : ({} as Component)
  }
  const getSourceName = (sources?: MissionSourceEnum[]) => {
    if (fromNav(sources)) return `l'unité`
    if (fromCnsp(completenessForStats?.sources)) return `le CNSP`
    if (fromCacem(completenessForStats?.sources)) return `le CACEM`
    return ``
  }

  const getMessageStatus = () => {
    if (isCompleteForStats(completenessForStats)) {
      return 'Les champs indispensables aux statistiques sont remplis.'
    }
    return `Des champs indispensables sont à remplir par ${getSourceName(completenessForStats?.sources)}.`
  }

  const fromNav = (sources?: MissionSourceEnum[]) => sources?.includes(MissionSourceEnum.RAPPORTNAV)
  const fromCnsp = (sources?: MissionSourceEnum[]) => sources?.includes(MissionSourceEnum.MONITORFISH)
  const fromCacem = (sources?: MissionSourceEnum[]) => sources?.includes(MissionSourceEnum.MONITORENV)
  const fromCnspAndCacem = (sources?: MissionSourceEnum[]) => fromCnsp(sources) && fromCacem(sources)

  const getBy = (sources?: MissionSourceEnum[]) => {
    if (fromCnspAndCacem(sources)) return `le CNSP et le CACEM`
    if (fromCnsp(completenessForStats?.sources)) return `le CNSP`
    if (fromCacem(completenessForStats?.sources)) return `le CACEM`
    return `votre unité`
  }

  const getBannerMessage = (): string => {
    if (isCompleteForStats(completenessForStats)) return MISSION_COMPLETED_MESSAGE
    return `${MISSION_INCOMPLETE_MESSAGE} par ${getBy(completenessForStats?.sources)}.`
  }

  const getBannerLevel = (): Level => {
    if (isCompleteForStats(completenessForStats)) return Level.SUCCESS
    if (fromNav(completenessForStats?.sources)) return Level.ERROR
    return Level.WARNING
  }

  return {
    isCompleteForStats,
    ...getCompletenessForStats(),
    bannerLevel: getBannerLevel(),
    bannerMessage: getBannerMessage(),
    statusMessage: getMessageStatus()
  }
}
