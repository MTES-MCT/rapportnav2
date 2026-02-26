export type MissionReportType = 'FIELD_REPORT' | 'OFFICE_REPORT' | 'EXTERNAL_REINFORCEMENT_TIME_REPORT'

export type MissionReinforcementType = 'PATROL' | 'JDP' | 'OTHER_ULAM' | 'SEA_TRAINER' | 'OTHER' | 'DIRM'

export type JdpType = 'DOCKED' | 'ONBOARD'

export type AdminGeneralInfos = {
  id?: number
  missionId?: number
  missionIdUUID?: string
  serviceId?: number
  distanceInNauticalMiles?: number
  consumedGOInLiters?: number
  consumedFuelInLiters?: number
  operatingCostsInEuro?: number
  fuelCostsInEuro?: number
  nbrOfRecognizedVessel?: number
  isWithInterMinisterialService?: boolean
  isMissionArmed?: boolean
  missionReportType?: MissionReportType
  reinforcementType?: MissionReinforcementType
  nbHourAtSea?: number
  jdpType?: JdpType
  isResourcesNotUsed?: boolean
  createdAt?: string
  updatedAt?: string
  createdBy?: number
  updatedBy?: number
}
