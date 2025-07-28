import { MissionAction } from './mission-action'

export enum InquiryOriginType {
  OPPORTUNITY_CONTROL = 'OPPORTUNITY_CONTROL',
  FOLLOW_UP_CONTROL = 'FOLLOW_UP_CONTROL',
  CNSP_REPORTING = 'CNSP_REPORTING',
  OTHER_REPORTING = 'OTHER_REPORTING',
  URCEM_DEDICATED_STATION = 'URCEM_DEDICATED_STATION'
}

export enum InquiryStatusType {
  NEW = 'NEW',
  CLOSED = 'CLOSED',
  FOLLOW_UP = 'FOLLOW_UP'
}

export enum InquiryConclusionType {
  NO_FOLLOW_UP = 'NO_FOLLOW_UP',
  WITH_REPORT = 'WITH_REPORT'
}

export enum InquiryTargetType {
  VEHICLE = 'VEHICLE',
  COMPANY = 'COMPANY'
}

export interface Inquiry {
  id?: string
  type?: string
  agentId?: number
  vesselId?: number
  serviceId?: number
  endDateTimeUtc?: string
  startDateTimeUtc?: string
  origin?: InquiryOriginType
  status?: InquiryStatusType
  conclusion?: InquiryConclusionType
  vesselName?: string
  isSignedByInspector?: boolean
  vesselExternalReferenceNumber?: string
  missionId?: number
  missionIdUUID?: string
  actions?: MissionAction[]
}
