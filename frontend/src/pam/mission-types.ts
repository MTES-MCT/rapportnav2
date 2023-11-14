import { ControlUnit } from './control-unit-types'
import {
  ActionTypeEnum,
  EnvAction,
  FormalNoticeEnum,
  Infraction as EnvInfraction,
  MissionSourceEnum,
  MissionTypeEnum,
  SeaFrontEnum,
  InfractionTypeEnum,
  VesselTypeEnum,
  VesselSizeEnum
} from './env-mission-types'
import { FishAction } from './fish-mission-types'
import { vesselTypeToHumanString } from './mission/controls/utils'

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

export const statusReasonToHumanString = (reason?: ActionStatusReason): string | undefined => {
  switch (reason) {
    case ActionStatusReason.MAINTENANCE:
      return 'Maintenance'
    case ActionStatusReason.WEATHER:
      return 'Météo'
    case ActionStatusReason.REPRESENTATION:
      return 'Représentation'
    case ActionStatusReason.ADMINISTRATION:
      return 'Administration'
    case ActionStatusReason.HARBOUR_CONTROL:
      return 'Contrôle portuaire'
    case ActionStatusReason.OTHER:
      return 'Autre'
    default:
      return undefined
  }
}

export const ACTION_STATUS_REASON_OPTIONS = [
  {
    label: statusReasonToHumanString(ActionStatusReason.MAINTENANCE),
    value: ActionStatusReason.MAINTENANCE
  },
  {
    label: statusReasonToHumanString(ActionStatusReason.WEATHER),
    value: ActionStatusReason.WEATHER
  },
  {
    label: statusReasonToHumanString(ActionStatusReason.REPRESENTATION),
    value: ActionStatusReason.REPRESENTATION
  },
  {
    label: statusReasonToHumanString(ActionStatusReason.ADMINISTRATION),
    value: ActionStatusReason.ADMINISTRATION
  },
  {
    label: statusReasonToHumanString(ActionStatusReason.HARBOUR_CONTROL),
    value: ActionStatusReason.HARBOUR_CONTROL
  },
  {
    label: statusReasonToHumanString(ActionStatusReason.OTHER),
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

export const VESSEL_TYPE_OPTIONS = [
  {
    label: vesselTypeToHumanString(VesselType.FISHING),
    value: VesselType.FISHING
  },
  {
    label: vesselTypeToHumanString(VesselType.SAILING),
    value: VesselType.SAILING
  },
  {
    label: vesselTypeToHumanString(VesselType.MOTOR),
    value: VesselType.MOTOR
  },
  {
    label: vesselTypeToHumanString(VesselType.COMMERCIAL),
    value: VesselType.COMMERCIAL
  },
  {
    label: vesselTypeToHumanString(VesselType.SAILING_LEISURE),
    value: VesselType.SAILING_LEISURE
  }
]

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

type ControlModel = {
  id: string
  amountOfControls: number
  unitShouldConfirm?: boolean
  unitHasConfirmed?: boolean
  observations?: string
  infractions?: Infraction[]
}

export type ControlAdministrative = ControlModel & {
  compliantOperatingPermit?: ControlResult
  upToDateNavigationPermit?: ControlResult
  compliantSecurityDocuments?: ControlResult
}
export type ControlGensDeMer = ControlModel & {
  staffOutnumbered?: ControlResult
  upToDateMedicalCheck?: ControlResult
  knowledgeOfFrenchLawAndLanguage?: ControlResult
}
export type ControlNavigation = ControlModel
export type ControlSecurity = ControlModel

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

export type Infraction = {
  id: string
  controlType: ControlType
  formalNotice?: FormalNoticeEnum
  infractions: Natinf[]
  observations?: string
  target?: InfractionTarget
}

export type InfractionTarget = {
  id: string
  natinfs?: Natinf[]
  observations?: string
  companyName?: string
  relevantCourt?: string
  infractionType?: InfractionTypeEnum
  formalNotice?: FormalNoticeEnum
  toProcess?: Boolean
  vesselType?: VesselTypeEnum
  vesselSize?: VesselSizeEnum
  vesselIdentifier?: string
  identityControlledPerson?: string
}

export type InfractionByTarget = {
  vesselIdentifier: string
  vesselType: VesselType
  infractions: Infraction[]
}

export type InfractionEnvNewTarget = Infraction & {
  controlType: ControlType
  identityControlledPerson: string
  vesselType: VesselType
  vesselSize: VesselSize
  vesselIdentifier: string
}

export type Natinf = {
  infraction: string
  // infractionCategory: string
  natinfCode: number
  // regulation: string
}
