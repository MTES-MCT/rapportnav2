import { ControlUnit } from './control-unit-types'
import { EnvAction, MissionSourceEnum, MissionTypeEnum, SeaFrontEnum } from './env-mission-types'
import { FishAction } from './fish-mission-types'

export enum ActionSource {
  'EnvAction' = 'EnvAction',
  'FishAction' = 'FishAction',
  'NavAction' = 'NavAction'
}

export type NavAction = {
  id: number
  actionType: any
  actionStartDateTimeUtc?: string | null
  actionEndDateTimeUtc?: string | null
  actionStatus: string
}
export type MissionActions = {
  envAction?: EnvAction
  fishAction?: FishAction
  navAction?: NavAction
}
export type MissionAction = EnvAction | FishAction | NavAction

export function isEnvAction(action: MissionAction): action is EnvAction {
  // TODO find a better way to segregate a env action
  return action !== null && typeof action === 'object' && 'actionNumberOfControls' in action
}

export function isFishAction(action: MissionAction): action is FishAction {
  // TODO find a better way to segregate a fish action
  return action !== null && typeof action === 'object' && 'actionDatetimeUtc' in action
}

export function isNavAction(action: MissionAction): action is NavAction {
  return !isEnvAction(action) && !isFishAction(action)
}

export type Mission = {
  id: number
  closedBy: string
  controlUnits: Omit<ControlUnit, 'id'>[]
  endDateTimeUtc?: string
  envActions: EnvAction[]
  facade: SeaFrontEnum
  geom?: Record<string, any>[]
  hasMissionOrder?: boolean
  isClosed: boolean
  isUnderJdp?: boolean
  missionSource: MissionSourceEnum
  missionTypes: MissionTypeEnum[]
  observationsCacem?: string
  observationsCnsp?: string
  openBy: string
  startDateTimeUtc: string
  actions: MissionActions[]
}

export enum ControlTarget {
  'PECHE_PRO' = 'PECHE_PRO',
  'PLAISANCE_PRO' = 'PLAISANCE_PRO',
  'COMMERCE_PRO' = 'COMMERCE_PRO',
  'SERVICE_PRO' = 'SERVICE_PRO',
  'PLAISANCE_LOISIR' = 'PLAISANCE_LOISIR'
}

export const getActionData = (action: MissionActions): [ActionSource, EnvAction | FishAction | NavAction] => {
  if (action.envAction && action.envAction) {
    return [ActionSource.EnvAction, action.envAction as EnvAction]
  } else if (action.fishAction && action.fishAction) {
    return [ActionSource.FishAction, action.fishAction as FishAction]
  }

  return [ActionSource.NavAction, action.navAction as NavAction]
}

export const getActionStartTime = (action: MissionActions): string | undefined | null => {
  if (action.envAction && action.envAction) {
    return action.envAction.actionStartDateTimeUtc
  } else if (action.fishAction && action.fishAction) {
    return action.fishAction.actionDatetimeUtc
  }
  return action.navAction?.actionStartDateTimeUtc
}
