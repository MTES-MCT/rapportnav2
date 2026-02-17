import { MissionSourceEnum } from '@common/types/env-mission-types.ts'
import { THEME } from '@mtes-mct/monitor-ui'

interface MissionTagHook {
  getTagTextColor: () => string
  getTagBorderColor: () => string
  getTagTextContent: () => string
  getTagBackgroundColor: () => string
  getOpenByText: (missionSource?: MissionSourceEnum) => string
}

export function useMissionTag(missionSource?: MissionSourceEnum): MissionTagHook {
  const getTagBackgroundColor = (): string => {
    switch (missionSource) {
      case MissionSourceEnum.MONITORENV:
      case MissionSourceEnum.POSEIDON_CACEM:
        return THEME.color.mediumSeaGreen
      case MissionSourceEnum.MONITORFISH:
      case MissionSourceEnum.POSEIDON_CNSP:
        return THEME.color.blueGray
      default:
        return THEME.color.gunMetal
    }
  }

  const getTagTextColor = (): string => THEME.color.white

  const getTagBorderColor = (): string => {
    return missionSource === MissionSourceEnum.RAPPORT_NAV ? THEME.color.white : 'transparent'
  }

  const getOpenByText = (source?: MissionSourceEnum): string => {
    switch (source) {
      case MissionSourceEnum.RAPPORT_NAV:
        return "Ouverte par l'unité"
      case MissionSourceEnum.MONITORENV:
      case MissionSourceEnum.POSEIDON_CACEM:
        return 'Ouverte par le CACEM'
      case MissionSourceEnum.MONITORFISH:
      case MissionSourceEnum.POSEIDON_CNSP:
        return 'Ouverte par le CNSP'
      default:
        return 'Ouverte par N/A'
    }
  }

  const getTagTextContent = (): string => getOpenByText(missionSource)

  return { getOpenByText, getTagTextColor, getTagBorderColor, getTagTextContent, getTagBackgroundColor }
}
