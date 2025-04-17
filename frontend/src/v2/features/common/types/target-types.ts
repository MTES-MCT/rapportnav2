import { ControlResult, ControlType } from '@common/types/control-types'
import { FormalNoticeEnum, InfractionTypeEnum, VesselSizeEnum } from '@common/types/env-mission-types'
import { VesselTypeEnum } from '@common/types/mission-types'
import { MissionSourceEnum } from './mission-types'

export enum TargetType {
  COMPANY = 'COMPANY',
  VEHICLE = 'VEHICLE',
  DEFAULT = 'DEFAULT',
  INDIVIDUAL = 'INDIVIDUAL'
}

export interface Infraction {
  id?: string
  observations?: string
  natinfs?: string[]
  infractionType?: InfractionTypeEnum
}

export interface Control {
  id?: string
  controlType: ControlType
  infractions?: Infraction[]
  observations?: string
  amountOfControls?: number
  compliantOperatingPermit?: ControlResult
  upToDateNavigationPermit?: ControlResult
  compliantSecurityDocuments?: ControlResult
  staffOutnumbered?: ControlResult
  upToDateMedicalCheck?: ControlResult
  hasBeenDone?: boolean
  knowledgeOfFrenchLawAndLanguage?: ControlResult
  nbrOfHours?: number
}

export interface TargetExternalData {
  id: string
  natinfs?: string[]
  observations?: string
  registrationNumber?: string
  companyName?: string
  relevantCourt?: string
  infractionType?: InfractionTypeEnum
  formalNotice?: FormalNoticeEnum
  toProcess?: boolean
  controlledPersonIdentity?: string
  vesselType?: VesselTypeEnum
  vesselSize?: VesselSizeEnum
}

export interface Target {
  id?: string
  actionId: string
  source: MissionSourceEnum
  targetType: TargetType
  agent?: string
  vesselName?: string
  vesselType?: VesselTypeEnum
  vesselSize?: VesselSizeEnum
  vesselIdentifier?: string
  startDateTimeUtc?: Date
  endDateTimeUtc?: Date
  identityControlledPerson?: string
  controls?: Control[]
  externalData?: TargetExternalData
}
