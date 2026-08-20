export enum VesselTypeEnum {
  FISHING = 'FISHING',
  SAILING = 'SAILING',
  MOTOR = 'MOTOR',
  COMMERCIAL = 'COMMERCIAL',
  SAILING_LEISURE = 'SAILING_LEISURE',
  SCHOOL = 'SCHOOL',
  PASSENGER = 'PASSENGER',
  SHELLFISH = 'SHELLFISH'
}

export enum VesselSizeEnum {
  LESS_THAN_12m = 'LESS_THAN_12m',
  FROM_12_TO_24m = 'FROM_12_TO_24m',
  FROM_24_TO_46m = 'FROM_24_TO_46m',
  MORE_THAN_46m = 'MORE_THAN_46m'
}

export interface Vessel {
  externalReferenceNumber: string
  flagState?: string
  internalReferenceNumber?: String
  vesselId: number
  vesselName?: string
}
