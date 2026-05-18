import { MissionSourceEnum } from '@common/types/env-mission-types'
import { Icon, IconProps, Level, THEME } from '@mtes-mct/monitor-ui'
import { FunctionComponent } from 'react'
import { CompletenessForStats, CompletenessForStatsStatusEnum, MissionStatusEnum } from '../types/mission-types.ts'

enum MissionStateType {
  MISSION_ENDED_AND_VALID = 'MISSION_ENDED_AND_VALID',
  MISSION_ENDED_AND_INVALID = 'MISSION_ENDED_AND_INVALID',
  MISSION_ENDED_AND_INCOMPLETE = 'MISSION_ENDED_AND_INCOMPLETE',
  MISSION_NOT_ENDED_AND_VALID = 'MISSION_NOT_ENDED_AND_VALID',
  MISSION_NOT_ENDED_AND_INVALID = 'MISSION_NOT_ENDED_AND_INVALID',
  MISSION_NOT_ENDED_AND_INCOMPLETE = 'MISSION_NOT_ENDED_AND_INCOMPLETE'
}

type Component = {
  text?: string
  color: string
  icon: FunctionComponent<IconProps>
}

type ComponentStateType = { [key in MissionStateType]: Component }

const MISSION_STATE_REGISTRY: ComponentStateType = {
  [MissionStateType.MISSION_ENDED_AND_VALID]: {
    text: 'Complété',
    icon: Icon.Confirm,
    color: THEME.color.mediumSeaGreen
  },
  [MissionStateType.MISSION_ENDED_AND_INVALID]: {
    text: 'Données invalides',
    icon: Icon.AttentionFilled,
    color: THEME.color.maximumRed
  },
  [MissionStateType.MISSION_ENDED_AND_INCOMPLETE]: {
    text: 'À compléter',
    icon: Icon.AttentionFilled,
    color: THEME.color.maximumRed
  },
  [MissionStateType.MISSION_NOT_ENDED_AND_VALID]: {
    text: 'Données à jour',
    icon: Icon.Confirm,
    color: THEME.color.mediumSeaGreen
  },
  [MissionStateType.MISSION_NOT_ENDED_AND_INVALID]: {
    text: 'Données invalides',
    icon: Icon.AttentionFilled,
    color: THEME.color.charcoal
  },
  [MissionStateType.MISSION_NOT_ENDED_AND_INCOMPLETE]: {
    text: 'À compléter',
    icon: Icon.AttentionFilled,
    color: THEME.color.charcoal
  }
}

const MISSION_INCOMPLETE_MESSAGE = `La mission est terminée mais le rapport n'est pas complet : des données sont manquantes dans des champs à remplir`
const MISSION_INVALID_MESSAGE = `La mission est terminée mais le rapport contient des données invalides à corriger`
const MISSION_COMPLETED_MESSAGE = `La mission est terminée et ses données sont complètes : le rapport est comptabilisé dans les statistiques. Vous pouvez désormais exporter le rapport de mission.`

type MissionCompletenessTagHook = {
  bannerLevel: Level
  bannerMessage: string
  statusMessage: string
  isCompleteForStats: (completenessForStats?: CompletenessForStats) => boolean
} & Component

export function useMissionCompletenessForStats(
  completenessForStats?: CompletenessForStats,
  isMissionFinished?: boolean,
  missionStatus?: MissionStatusEnum
): MissionCompletenessTagHook {
  const isCompleteForStats = (completenessForStats?: CompletenessForStats) =>
    completenessForStats?.status === CompletenessForStatsStatusEnum.VALID

  const isInvalid = (completenessForStats?: CompletenessForStats) =>
    completenessForStats?.status === CompletenessForStatsStatusEnum.INVALID

  const isMissionEnded = isMissionFinished || missionStatus === MissionStatusEnum.ENDED

  const getMissionStateType = (): MissionStateType => {
    if (isMissionEnded) {
      if (isCompleteForStats(completenessForStats)) return MissionStateType.MISSION_ENDED_AND_VALID
      if (isInvalid(completenessForStats)) return MissionStateType.MISSION_ENDED_AND_INVALID
      return MissionStateType.MISSION_ENDED_AND_INCOMPLETE
    }
    if (isCompleteForStats(completenessForStats)) return MissionStateType.MISSION_NOT_ENDED_AND_VALID
    if (isInvalid(completenessForStats)) return MissionStateType.MISSION_NOT_ENDED_AND_INVALID
    return MissionStateType.MISSION_NOT_ENDED_AND_INCOMPLETE
  }

  const getCompletenessForStats = (): Component => {
    const missionStateType = getMissionStateType()
    return MISSION_STATE_REGISTRY[missionStateType]
  }
  const getSourceName = (sources?: MissionSourceEnum[]) => {
    if (fromNav(sources)) return `l'unité`
    if (fromCnsp(completenessForStats?.sources)) return `le CNSP`
    if (fromCacem(completenessForStats?.sources)) return `le CACEM`
    return ``
  }

  const getMessageStatus = () => {
    if (!completenessForStats || isCompleteForStats(completenessForStats)) {
      return 'Les champs indispensables aux statistiques sont remplis.'
    }
    if (isInvalid(completenessForStats)) {
      return `Des données sont invalides et doivent être corrigées par ${getSourceName(completenessForStats?.sources)}.`
    }
    return `Des champs indispensables sont à remplir par ${getSourceName(completenessForStats?.sources)}.`
  }

  const fromNav = (sources?: MissionSourceEnum[]) => sources?.includes(MissionSourceEnum.RAPPORT_NAV)
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
    if (isInvalid(completenessForStats)) {
      return `${MISSION_INVALID_MESSAGE} par ${getBy(completenessForStats?.sources)}.`
    }
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
