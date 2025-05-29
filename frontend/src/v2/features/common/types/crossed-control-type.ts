export enum CrossControlOriginType {
  OPPORTUNITY_CONTROL = 'OPPORTUNITY_CONTROL',
  FOLLOW_UP_CONTROL = 'FOLLOW_UP_CONTROL',
  CNSP_REPORTING = 'CNSP_REPORTING',
  OTHER_REPORTING = 'OTHER_REPORTING',
  URCEM_DEDICATED_STATION = 'URCEM_DEDICATED_STATION'
}

export enum CrossControlStatusType {
  NEW = 'NEW',
  FOLLOW_UP = 'FOLLOW_UP',
  CLOSED = 'CLOSED'
}

export enum CrossControlTargetType {
  VEHICLE = 'VEHICLE',
  COMPANY = 'COMPANY'
}

export enum CrossControlConclusionType {
  NO_FOLLOW_UP = 'NO_FOLLOW_UP',
  WITH_REPORT = 'WITH_REPORT'
}

export interface CrossControl {
  id?: string
  agentId?: number
  vesselId?: number
  serviceId?: number
  nbrOfHours?: number
  endDateTimeUtc?: string
  startDateTimeUtc?: string
  isSignedByInspector?: boolean
  type?: CrossControlTargetType
  origin?: CrossControlOriginType
  status?: CrossControlStatusType
  conclusion?: CrossControlConclusionType
  vesselName?: string
  isReferentialClosed?: boolean
  vesselExternalReferenceNumber?: string
}
