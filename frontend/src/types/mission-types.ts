import { Action } from './action-types'
import { ControlUnit } from './control-unit-types'
import { MissionSourceEnum, MissionTypeEnum, SeaFrontEnum } from './env-mission-types'

export enum VesselTypeEnum {
  'FISHING' = 'FISHING',
  'SAILING' = 'SAILING',
  'MOTOR' = 'MOTOR',
  'COMMERCIAL' = 'COMMERCIAL',
  'SAILING_LEISURE' = 'SAILING_LEISURE'
}

export enum VesselSizeEnum {
  'LESS_THAN_12m' = 'LESS_THAN_12m',
  'FROM_12_TO_24m' = 'FROM_12_TO_24m',
  'FROM_24_TO_46m' = 'FROM_24_TO_46m',
  'MORE_THAN_46m' = 'MORE_THAN_46m'
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
  generalInfo: MissionGeneralInfo
  status: MissionStatusEnum
  reportStatus?: MissionReportStatus
}

export type MissionGeneralInfo = {
  id: number
  distanceInNauticalMiles?: number
  consumedGOInLiters?: number
  consumedFuelInLiters?: number
}

export type MissionExport = {
  fileName: string
  fileContent: string
}

export type MissionReportStatus = {
  status: MissionReportStatusEnum
  sources?: MissionSourceEnum[]
}

export enum MissionStatusEnum {
  UPCOMING = 'UPCOMING',
  PENDING = 'PENDING',
  IN_PROGRESS = 'IN_PROGRESS',
  CLOSED = 'CLOSED',
  ENDED = 'ENDED',
  UNAVAILABLE = 'UNAVAILABLE'
}

export enum MissionReportStatusEnum {
  COMPLETE = 'COMPLETE',
  INCOMPLETE = 'INCOMPLETE'
}
