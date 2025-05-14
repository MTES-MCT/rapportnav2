import { ControlResource, ControlUnit } from '@common/types/control-unit-types.ts'
import { MissionCrew } from '@common/types/crew-types'
import { MissionTypeEnum, SeaFrontEnum } from '@common/types/env-mission-types'
import { Service } from '@common/types/service-types'
import { MissionAction } from './mission-action'
import { ControlUnitResource } from './control-unit-types.ts'

export enum MissionType {
  AIR = 'AIR',
  LAND = 'LAND',
  SEA = 'SEA'
}

export type MissionGeneralInfoPam = {
  id?: number
  distanceInNauticalMiles?: number
  consumedGOInLiters?: number
  consumedFuelInLiters?: number
  serviceId?: number
  nbrOfRecognizedVessel?: number
}

export type MissionULAMGeneralInfoInitial = {
  id?: number
  startDateTimeUtc: string
  endDateTimeUtc: string
  missionTypes: MissionTypeEnum[]
  missionReportType?: MissionReportTypeEnum
  reinforcementType?: MissionReinforcementTypeEnum
  nbHourAtSea?: number
}

export type MissionGeneralInfoExtended = {
  resources?: ControlResource[]
  crew?: MissionCrew[]
  isMissionArmed?: boolean
  isWithInterMinisterialService?: boolean
  observations?: string
  isAllAgentsParticipating?: boolean
  interMinisterialServices?: InterMinisterialService[]
  isUnderJdp?: boolean
  jdpType?: JdpTypeEnum
}

export enum MissionReportTypeEnum {
  FIELD_REPORT = 'FIELD_REPORT',
  OFFICE_REPORT = 'OFFICE_REPORT',
  EXTERNAL_REINFORCEMENT_TIME_REPORT = 'EXTERNAL_REINFORCEMENT_TIME_REPORT'
}

export enum JdpTypeEnum {
  DOCKED = 'DOCKED',
  ONBOARD = 'ONBOARD',
}

export enum MissionReinforcementTypeEnum {
  PATROL = 'PATROL',
  OTHER_ULAM = 'OTHER_ULAM',
  SEA_TRAINER = 'SEA_TRAINER',
  OTHER = 'OTHER',
  DIRM = 'DIRM'
}

export enum MissionSourceEnum {
  MONITORENV = 'MONITORENV',
  MONITORFISH = 'MONITORFISH',
  POSEIDON_CACEM = 'POSEIDON_CACEM',
  POSEIDON_CNSP = 'POSEIDON_CNSP',
  RAPPORTNAV = 'RAPPORTNAV'
}

export enum CompletenessForStatsStatusEnum {
  COMPLETE = 'COMPLETE',
  INCOMPLETE = 'INCOMPLETE'
}

export enum MissionStatusEnum {
  UPCOMING = 'UPCOMING',
  IN_PROGRESS = 'IN_PROGRESS',
  ENDED = 'ENDED',
  UNAVAILABLE = 'UNAVAILABLE'
}

export type Mission = {
  id?: number
  missionTypes: MissionType[]
  controlUnits: ControlUnit[]
  openBy?: string
  completedBy?: string
  observationsCacem?: string
  observationsCnsp?: string
  facades?: string
  geom?: string
  startDateTimeUtc: string
  endDateTimeUtc?: string
  missionSource: MissionSourceEnum
  hasMissionOrder: boolean
  isUnderJdp: boolean
  isGeometryComputedFromControls: boolean
}

export type CompletenessForStats = {
  sources?: MissionSourceEnum[]
  status?: CompletenessForStatsStatusEnum
}

export type MissionEnvData = {
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
}

export type MissionGeneralInfo2 = MissionGeneralInfoPam & {
  missionReportType?: MissionReportTypeEnum
  missionTypes: MissionTypeEnum[]
  reinforcementType?: MissionReinforcementTypeEnum
  nbHourAtSea?: number
  crew?: MissionCrew[]
  services?: Service[]
  isMissionArmed?: boolean
  isWithInterMinisterialService?: boolean
  isAllAgentsParticipating?: boolean
  missionId?: number
  startDateTimeUtc: string
  endDateTimeUtc: string
  observations?: string
  resources?: ControlResource[]
  interMinisterialServices?: InterMinisterialService[]
  missionIdString?: string
  isUnderJdp?: boolean
  jdpType?: JdpTypeEnum
}

export type Mission2 = {
  id: number
  isCompleteForStats: boolean
  status: MissionStatusEnum
  envData: MissionEnvData
  generalInfos: MissionGeneralInfo2
  completenessForStats?: CompletenessForStats
  actions: MissionAction[]
  missionIdString?: string
}

export type MissionListItem = {
  id: number
  openBy?: string
  exportLabel?: string
  status?: MissionStatusEnum
  missionNameUlam?: string
  startDateTimeUtc: string
  startDateTimeUtcText?: string
  crew?: MissionCrew[]
  crewNumber?: string
  endDateTimeUtc?: string
  endDateTimeUtcText?: string
  missionNamePam?: string
  observationsByUnit?: string
  missionSource?: MissionSourceEnum
  completenessForStats?: CompletenessForStats
  controlUnits?: ControlUnit[]
  resources?: ControlUnitResource[],
  missionReportType?: MissionReportTypeEnum
  missionIdString?: string
  isUnderJdp?: boolean
  jdpType?: JdpTypeEnum
}

export type InterMinisterialService = {
  administrationId: number
  controlUnitId: number
}
