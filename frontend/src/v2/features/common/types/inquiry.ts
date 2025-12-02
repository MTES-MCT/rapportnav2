import { MissionAction } from './mission-action'
import { Vessel } from './vessel-type'

export enum InquiryOriginType {
  OPPORTUNITY_CONTROL = 'OPPORTUNITY_CONTROL',
  FOLLOW_UP_CONTROL = 'FOLLOW_UP_CONTROL',
  CNSP_REPORTING = 'CNSP_REPORTING',
  OTHER_REPORTING = 'OTHER_REPORTING',
  URCEM_DEDICATED_STATION = 'URCEM_DEDICATED_STATION'
}

export enum InquiryStatusType {
  CLOSED = 'CLOSED',
  IN_PROGRESS = 'IN_PROGRESS'
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
  type?: InquiryTargetType
  agentId?: number
  serviceId?: number
  endDateTimeUtc?: string
  startDateTimeUtc?: string
  origin?: InquiryOriginType
  status?: InquiryStatusType
  conclusion?: InquiryConclusionType
  isSignedByInspector?: boolean
  missionId?: number
  siren?: string
  missionIdUUID?: string
  actions?: MissionAction[]
  vessel?: Vessel
  isForeignEstablishment?: boolean
}
