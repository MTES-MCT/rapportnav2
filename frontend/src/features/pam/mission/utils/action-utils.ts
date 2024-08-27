import { Action } from '../../../common/types/action-types.ts'
import { MissionSourceEnum } from '../../../common/types/env-mission-types.ts'

export function isEnvAction(action: Action): boolean {
  return action !== null && action.source === MissionSourceEnum.MONITORENV
}

export function isFishAction(action: Action): boolean {
  return action !== null && action.source === MissionSourceEnum.MONITORFISH
}

export function isNavAction(action: Action): boolean {
  return action !== null && action.source === MissionSourceEnum.RAPPORTNAV
}

export const vesselNameOrUnknown = (name?: string): string | undefined => {
  if (!name) {
    return
  } else if (name === 'UNKNOWN') {
    return 'Navire inconnu'
  } else {
    return name
  }
}
