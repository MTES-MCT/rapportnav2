import { MissionSourceEnum } from '@common/types/env-mission-types.ts'
import { THEME } from '@mtes-mct/monitor-ui'

export const getOpenByText = (missionSource?: MissionSourceEnum): string => {
  switch (missionSource) {
    case MissionSourceEnum.RAPPORTNAV:
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

interface MissionTagHook {
  getTagTextColor: () => string
  getTagBorderColor: () => string
  getTagTextContent: () => string
  getTagBackgroundColor: () => string
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
    return missionSource === MissionSourceEnum.RAPPORTNAV ? THEME.color.white : 'transparent'
  }

  const getTagTextContent = (): string => getOpenByText(missionSource)

  return { getTagTextColor, getTagBorderColor, getTagTextContent, getTagBackgroundColor }
}
