import { ControlUnit } from './control-unit-types'
import { ActionTypeEnum, EnvAction, MissionSourceEnum, MissionTypeEnum, SeaFrontEnum } from './env-mission-types'
import { FishAction, MissionActionType } from './fish-mission-types'

export enum ActionSource {
  'EnvAction' = 'EnvAction',
  'FishAction' = 'FishAction',
  'NavAction' = 'NavAction'
}

export enum ActionStatusType {
  'NAVIGATING' = 'NAVIGATING',
  'DOCKING' = 'DOCKING',
  'ANCHORING' = 'ANCHORING',
  'UNAVAILABLE' = 'UNAVAILABLE'
}

export type NavAction = {
  id: number
  actionType: any
  actionStartDateTimeUtc?: string | null
  actionEndDateTimeUtc?: string | null
  actionStatus: string
}
export type Action = {
  id?: any
  missionId: number
  type: ActionTypeEnum
  source: MissionSourceEnum
  status: ActionStatusType
  startDateTimeUtc?: string
  endDateTimeUtc?: string
  data: [EnvAction | FishAction | NavAction]
}

export function isEnvAction(action: Action): boolean {
  return action !== null && action.source === MissionSourceEnum.MONITORENV
}

export function isFishAction(action: Action): boolean {
  return action !== null && action.source === MissionSourceEnum.MONITORFISH
}

export function isNavAction(action: Action): boolean {
  return action !== null && action.source === MissionSourceEnum.RAPPORTNAV
}

export type Mission = {
  id: number
  closedBy: string
  controlUnits: Omit<ControlUnit, 'id'>[]
  endDateTimeUtc?: string
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
  actions: Action[]
}

export enum ControlTarget {
  'PECHE_PRO' = 'PECHE_PRO',
  'PLAISANCE_PRO' = 'PLAISANCE_PRO',
  'COMMERCE_PRO' = 'COMMERCE_PRO',
  'SERVICE_PRO' = 'SERVICE_PRO',
  'PLAISANCE_LOISIR' = 'PLAISANCE_LOISIR'
}
