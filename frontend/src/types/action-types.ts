import {
    ControlAdministrative,
    ControlGensDeMer,
    ControlMethod,
    ControlNavigation,
    ControlSecurity,
    ControlType
} from './control-types'
import { ActionTypeEnum, EnvAction, MissionSourceEnum, VesselSizeEnum, VesselTypeEnum } from './env-mission-types'
import { FishAction } from './fish-mission-types'

export type Action = {
    id?: any
    missionId: number
    type: ActionTypeEnum
    source: MissionSourceEnum
    status: ActionStatusType
    startDateTimeUtc?: string
    endDateTimeUtc?: string
    summaryTags?: string[]
    controlsToComplete?: ControlType[]
    data: [EnvAction | FishAction | ActionStatus | ActionControl | ActionFreeNote | ActionSurveillance | ActionRescue | ActionNauticalEvent | ActionVigimer | ActionAntiPollution | ActionBAAEMPermanence | ActionRepresentation | ActionPublicOrder | ActionIllegalImmigration]
}

export type ActionStatus = {
    id: string
    status: ActionStatusType
    startDateTimeUtc: string
    reason?: ActionStatusReason
    observations?: string
}

export type ActionControl = {
    latitude: number
    longitude: number
    controlMethod?: ControlMethod
    vesselIdentifier?: string
    vesselType?: VesselTypeEnum
    vesselSize?: VesselSizeEnum
    observations?: string
    identityControlledPerson?: string
    controlAdministrative?: ControlAdministrative
    controlGensDeMer?: ControlGensDeMer
    controlNavigation?: ControlNavigation
    controlSecurity?: ControlSecurity
}

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
    'TECHNICAL' = 'TECHNICAL',
    'PERSONNEL' = 'PERSONNEL',
    'OTHER' = 'OTHER'
}


export type ActionFreeNote = {
    id: string
    startDateTimeUtc: string
    observations?: string
}

export type ActionSurveillance = {
  id: string
  startDateTimeUtc: string
  endDateTimeUtc: string
  observations?: string
}


export type ActionRescue = {
  id: string
  startDateTimeUtc: string
  endDateTimeUtc: string
  longitude: number
  latitude: number
  isVesselRescue: boolean
  isPersonRescue: boolean
  isInSRRorFollowedByCROSSMRCC: boolean
  isVesselNoticed: boolean
  isVesselTowed: boolean
  numberPersonsRescued: number
  numberOfDeaths: number
  isOperationFollowedByDEFREP: boolean
  observations?: string
  locationDescription?: string
}

export type ActionNauticalEvent = {
  id: string
  startDateTimeUtc: string
  endDateTimeUtc: string
  observations: string
}

export type ActionVigimer = {
  id: string
  startDateTimeUtc: string
  endDateTimeUtc: string
  observations: string
}

export type ActionAntiPollution = {
  id: string
  startDateTimeUtc: string
  endDateTimeUtc: string
  observations: string
}

export type ActionBAAEMPermanence = {
  id: string
  startDateTimeUtc: string
  endDateTimeUtc: string
  observations: string
}

export type ActionPublicOrder = {
  id: string
  startDateTimeUtc: string
  endDateTimeUtc: string
  observations: string
}

export type ActionRepresentation = {
  id: string
  startDateTimeUtc: string
  endDateTimeUtc: string
  observations: string
}

export type ActionIllegalImmigration = {
  id: string
  startDateTimeUtc: string
  endDateTimeUtc: string
  observations: string
  nbOfInterceptedVessels?: number
  nbOfInterceptedMigrants?: number
  nbOfSuspectedSmugglers?: number
  latitude?: number
  longitude?: number
}
