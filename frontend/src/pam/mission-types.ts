import { ControlUnit } from './control-unit-types'
import { ActionTypeEnum, EnvAction, MissionSourceEnum, MissionTypeEnum, SeaFrontEnum } from './env-mission-types'
import { FishAction } from './fish-mission-types'

export enum ActionSource {
  'EnvAction' = 'EnvAction',
  'FishAction' = 'FishAction',
  'NavAction' = 'NavAction'
}

export enum ActionStatusType {
  'NAVIGATING' = 'NAVIGATING',
  'DOCKED' = 'DOCKED',
  'ANCHORED' = 'ANCHORED',
  'UNAVAILABLE' = 'UNAVAILABLE',
  'UNKNOWN' = 'UNKNOWN'
}

export enum ActionStatusReason {
  'MAINTENANCE' = 'MAINTENANCE',
  'WEATHER' = 'WEATHER',
  'REPRESENTATION' = 'REPRESENTATION',
  'ADMINISTRATION' = 'ADMINISTRATION',
  'HARBOUR_CONTROL' = 'HARBOUR_CONTROL',
  'OTHER' = 'OTHER'
}

export const ACTION_STATUS_REASON_OPTIONS = [
  {
    label: 'Maintenace',
    value: ActionStatusReason.MAINTENANCE
  },
  {
    label: 'Météo',
    value: ActionStatusReason.WEATHER
  },
  {
    label: 'Représentation',
    value: ActionStatusReason.REPRESENTATION
  },
  {
    label: 'Administration',
    value: ActionStatusReason.ADMINISTRATION
  },
  {
    label: 'Contrôle portuaire',
    value: ActionStatusReason.HARBOUR_CONTROL
  },
  {
    label: 'Autre',
    value: ActionStatusReason.OTHER
  }
]

export enum VesselType {
  'FISHING' = 'FISHING',
  'SAILING' = 'SAILING',
  'MOTOR' = 'MOTOR',
  'COMMERCIAL' = 'COMMERCIAL',
  'SAILING_LEISURE' = 'SAILING_LEISURE'
}

export enum VesselSize {
  'LESS_THAN_12m' = 'LESS_THAN_12m',
  'FROM_12_TO_24m' = 'FROM_12_TO_24m',
  'FROM_24_TO_46m' = 'FROM_24_TO_46m',
  'MORE_THAN_46m' = 'MORE_THAN_46m'
}

export const VESSEL_SIZE_OPTIONS = [
  {
    label: 'Moins de 12m',
    value: VesselSize.LESS_THAN_12m
  },
  {
    label: 'Entre 12m et 24m',
    value: VesselSize.FROM_12_TO_24m
  },
  {
    label: 'Entre 24m et 46m',
    value: VesselSize.FROM_24_TO_46m
  },
  {
    label: 'Plus de 46m',
    value: VesselSize.MORE_THAN_46m
  }
]

export enum ControlMethod {
  'SEA' = 'SEA',
  'AIR' = 'AIR',
  'LAND' = 'LAND'
}

export type Action = {
  id?: any
  missionId: number
  type: ActionTypeEnum
  source: MissionSourceEnum
  status: ActionStatusType
  startDateTimeUtc?: string
  endDateTimeUtc?: string
  data: [EnvAction | FishAction | ActionStatus | ActionControl]
}

export type ActionStatus = {
  id: string
  status: ActionStatusType
  startDateTimeUtc: string
  isStart: boolean
  reason?: ActionStatusReason
  observations?: string
}

export type ActionControl = {
  latitude: number
  longitude: number
  controlMethod?: ControlMethod
  vesselIdentifier?: string
  vesselType?: VesselType
  vesselSize?: VesselSize
  observations?: string
  identityControlledPerson?: string
  controlAdministrative?: ControlAdministrative
  controlGensDeMer?: ControlGensDeMer
  controlNavigation?: ControlNavigation
  controlSecurity?: ControlSecurity
}

export enum ControlResult {
  'YES' = 'YES',
  'NO' = 'NO',
  'NOT_CONTROLLED' = 'NOT_CONTROLLED',
  'NOT_CONCERNED' = 'NOT_CONCERNED'
}

export enum ControlType {
  'ADMINISTRATIVE' = 'ADMINISTRATIVE',
  'GENS_DE_MER' = 'GENS_DE_MER',
  'SECURITY' = 'SECURITY',
  'NAVIGATION' = 'NAVIGATION'
}

export type ControlAdministrative = {
  id: String
  amountOfControls: number
  unitShouldConfirm?: boolean
  unitHasConfirmed?: boolean
  compliantOperatingPermit?: ControlResult
  upToDateNavigationPermit?: ControlResult
  compliantSecurityDocuments?: ControlResult
  observations?: string
  deletedAt?: string
}
export type ControlGensDeMer = {
  id: String
  amountOfControls: number
  unitShouldConfirm?: boolean
  unitHasConfirmed?: boolean
  staffOutnumbered?: ControlResult
  upToDateMedicalCheck?: ControlResult
  knowledgeOfFrenchLawAndLanguage?: ControlResult
  observations?: string
  deletedAt?: string
}
export type ControlNavigation = {
  id: String
  amountOfControls: number
  unitShouldConfirm?: boolean
  unitHasConfirmed?: boolean
  observations?: string
  deletedAt?: string
}
export type ControlSecurity = {
  id: String
  amountOfControls: number
  unitShouldConfirm?: boolean
  unitHasConfirmed?: boolean
  observations?: string
  deletedAt?: string
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

// export enum ControlTarget {
//   'PECHE_PRO' = 'PECHE_PRO',
//   'PLAISANCE_PRO' = 'PLAISANCE_PRO',
//   'COMMERCE_PRO' = 'COMMERCE_PRO',
//   'SERVICE_PRO' = 'SERVICE_PRO',
//   'PLAISANCE_LOISIR' = 'PLAISANCE_LOISIR'
// }

// export enum ControlTargetText {
//   'PECHE_PRO' = 'pêche professionnelle',
//   'PLAISANCE_PRO' = 'plaisance professionnelle',
//   'COMMERCE_PRO' = 'commerce',
//   'SERVICE_PRO' = 'service',
//   'PLAISANCE_LOISIR' = 'plaisance de loisir'
// }
