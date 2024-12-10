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




