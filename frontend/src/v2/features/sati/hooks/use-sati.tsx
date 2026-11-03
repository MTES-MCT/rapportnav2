import { MissionActionType } from '@common/types/fish-mission-types.ts'
import { useSelector } from '@tanstack/react-store'
import { store } from '../../../store'
import { ActionType } from '../../common/types/action-type.ts'
import { MissionAction, MissionFishActionData } from '../../common/types/mission-action.ts'
import { MissionSourceEnum } from '../../common/types/mission-types.ts'

const _satiEnvRaw: string = import.meta.env.SATI_ENABLED_SERVICES ?? ''

console.log('_satiEnvRaw', _satiEnvRaw)

export const SATI_ENABLED_SERVICES: number[] = _satiEnvRaw
  .split(',')
  .map(Number)
  .filter(n => !isNaN(n) && n > 0)

// Mirrors backend SatiModuleType.fromMissionActionType: only sea (M1) and land (M3) fish controls have a SATI
const FISH_ACTION_TYPES_WITH_SATI = [MissionActionType.SEA_CONTROL, MissionActionType.LAND_CONTROL]

export const hasSatiPdf = (action: MissionAction): boolean =>
  action.source === MissionSourceEnum.MONITORFISH &&
  action.actionType === ActionType.CONTROL &&
  FISH_ACTION_TYPES_WITH_SATI.includes((action.data as MissionFishActionData).fishActionType)

interface SatiHook {
  isSatiEnabled: boolean
  canExportSatiPdf: (action: MissionAction) => boolean
}

export function useSati(): SatiHook {
  const user = useSelector(store, state => state.user)

  const isServiceIdIsEnabled = (): boolean => {
    const serviceId = user?.serviceId
    return !!serviceId && SATI_ENABLED_SERVICES.includes(serviceId)
  }

  const isSatiEnabled = isServiceIdIsEnabled()

  return {
    isSatiEnabled,
    canExportSatiPdf: (action: MissionAction) => isSatiEnabled && hasSatiPdf(action)
  }
}
