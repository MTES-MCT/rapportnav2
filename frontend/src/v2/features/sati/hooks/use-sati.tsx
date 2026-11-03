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

// Mirrors backend ExportSatiPdf: for now the pdf only exists for fish sea controls (SATI module M1)
const FISH_ACTION_TYPES_WITH_SATI_PDF = [MissionActionType.SEA_CONTROL]

export const hasSatiPdf = (action: MissionAction): boolean =>
  action.source === MissionSourceEnum.MONITORFISH &&
  action.actionType === ActionType.CONTROL &&
  FISH_ACTION_TYPES_WITH_SATI_PDF.includes((action.data as MissionFishActionData).fishActionType)

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
