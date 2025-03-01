import { Action } from './action-types.ts'
import { ControlUnit } from './control-unit-types.ts'
import { MissionSourceEnum, MissionTypeEnum, SeaFrontEnum } from './env-mission-types.ts'
import { Service } from './service-types.ts'

export enum VesselTypeEnum {
  'FISHING' = 'FISHING',
  'SAILING' = 'SAILING',
  'MOTOR' = 'MOTOR',
  'COMMERCIAL' = 'COMMERCIAL',
  'SAILING_LEISURE' = 'SAILING_LEISURE',
  'SCHOOL' = 'SCHOOL',
  'PASSENGER' = 'PASSENGER'
}

export enum VesselSizeEnum {
  'LESS_THAN_12m' = 'LESS_THAN_12m',
  'FROM_12_TO_24m' = 'FROM_12_TO_24m',
  'FROM_24_TO_46m' = 'FROM_24_TO_46m',
  'MORE_THAN_46m' = 'MORE_THAN_46m'
}

export type Mission = {
  id: number
  controlUnits: Omit<ControlUnit, 'id'>[]
  endDateTimeUtc?: string
  facade: SeaFrontEnum
  geom?: Record<string, any>[]
  hasMissionOrder?: boolean
  isUnderJdp?: boolean
  missionSource: MissionSourceEnum
  missionTypes: MissionTypeEnum[]
  observationsCacem?: string
  observationsCnsp?: string
  observationsByUnit?: string
  openBy: string
  startDateTimeUtc: string
  actions: Action[]
  generalInfo: MissionGeneralInfo
  status: MissionStatusEnum
  completenessForStats?: CompletenessForStats
  services: Service[]
}

export type MissionGeneralInfo = {
  id: number
  distanceInNauticalMiles?: number
  consumedGOInLiters?: number
  consumedFuelInLiters?: number
  serviceId?: number
  nbrOfRecognizedVessel?: number
}

export type MissionExport = {
  fileName: string
  fileContent: string
}

export type CompletenessForStats = {
  status?: CompletenessForStatsStatusEnum
  sources?: MissionSourceEnum[]
}

export enum MissionStatusEnum {
  UPCOMING = 'UPCOMING',
  IN_PROGRESS = 'IN_PROGRESS',
  ENDED = 'ENDED',
  UNAVAILABLE = 'UNAVAILABLE'
}

export enum CompletenessForStatsStatusEnum {
  COMPLETE = 'COMPLETE',
  INCOMPLETE = 'INCOMPLETE'
}
