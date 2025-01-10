import { ControlUnit } from '@common/types/control-unit-types.ts'

export enum MissionTypeEnum {
  AIR = 'AIR',
  LAND = 'LAND',
  SEA = 'SEA'
}

export type MissionULAMGeneralInfoInitial = {
  startDateTimeUtc: string
  endDateTimeUtc: string
  missionTypes: MissionTypeEnum[]
  missionReportType?: MissionReportTypeEnum
  reinforcementType?: MissionReinforcementTypeEnum
  nbHourAtSea?: number
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

export enum MissionSource {
  MONITORENV = 'MONITORENV',
  MONITORFISH = 'MONITORFISH',
  POSEIDON_CACEM = 'POSEIDON_CACEM',
  POSEIDON_CNSP = 'POSEIDON_CNSP',
  RAPPORTNAV = 'RAPPORTNAV'
}

export type MissionEnv = {
  id?: number
  missionTypes: MissionTypeEnum[]
  controlUnits: ControlUnit[]
  openBy?: string
  completedBy?: string
  observationsCacem?: string
  observationsCnsp?: string
  facades?: string
  geom?: string
  startDateTimeUtc: string
  endDateTimeUtc?: string
  missionSource: MissionSource
  hasMissionOrder: boolean
  isUnderJdp: boolean
  isGeometryComputedFromControls: boolean
}
