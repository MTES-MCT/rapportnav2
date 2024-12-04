import { Action } from './action-types.ts'
import { ControlUnit } from './control-unit-types.ts'
import { MissionSourceEnum, MissionTypeEnum, SeaFrontEnum } from './env-mission-types.ts'
import { Service } from './service-types.ts'
import { MissionCrew } from '@common/types/crew-types.ts'

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
  crew: MissionCrew[]
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

export type MissionULAMGeneralInfoInitial = {
  startDateTimeUtc: string
  endDateTimeUtc: string
  missionType: MissionTypeEnum[]
  missionReportType?: MissionReportTypeEnum
  reinforcementType?: MissionReinforcementTypeEnum
  nbHourAtSea?: number
}

export type MissionULAMGeneralInfoInitialInput = {
  dates: Date[]
  endDateTimeUtc: string
  missionType: MissionTypeEnum[]
  missionReportType?: MissionReportTypeEnum
  reinforcementType?: MissionReinforcementTypeEnum
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

export enum MissionReportTypeEnum {
  FIELD_REPORT = 'FIELD_REPORT',
  OFFICE_REPORT = 'OFFICE_REPORT',
  EXTERNAL_REINFORCEMENT_TIME_REPORT = 'EXTERNAL_REINFORCEMENT_TIME_REPORT'
}

export enum MissionReinforcementTypeEnum {
  PATROL = 'PATROL',
  JDP = 'JDP',
  OTHER_ULAM = 'OTHER_ULAM',
  SEA_TRAINER = 'SEA_TRAINER',
  OTHER = 'OTHER',
  DIRM = 'DIRM'
}

export const REPORT_TYPE_OPTIONS = [
  { label: 'Rapport avec sortie terrain', value: MissionReportTypeEnum.FIELD_REPORT },
  { label: 'Rapport sans sortie terrain (admin. uniquement)', value: MissionReportTypeEnum.OFFICE_REPORT },
  { label: 'Rapport de temps agent en renfort extérieur', value: MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT },
]

export const MISSION_TYPE_OPTIONS = [
  { label: 'Terre', value: MissionTypeEnum.LAND },
  { label: 'Mer', value: MissionTypeEnum.SEA },
  { label: 'Air', value: MissionTypeEnum.AIR },
]

export const REINFORCEMENT_TYPE = [
  { label: 'Patrouille (mission PAM)', value: MissionReinforcementTypeEnum.PATROL },
  { label: 'JDP', value: MissionReinforcementTypeEnum.JDP },
  { label: 'DIRM', value: MissionReinforcementTypeEnum.DIRM },
  { label: 'Autre ULAM', value: MissionReinforcementTypeEnum.OTHER_ULAM },
  { label: 'Formateur ESP Mer', value: MissionReinforcementTypeEnum.SEA_TRAINER },
  { label: 'Autre', value: MissionReinforcementTypeEnum.OTHER },
]
