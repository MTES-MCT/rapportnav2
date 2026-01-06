import { ControlType } from './control-types.ts'
import {
  FormalNoticeEnum,
  InfractionTypeEnum,
  VehicleTypeEnum,
  VesselSizeEnum,
  VesselTypeEnum
} from './env-mission-types.ts'

export type Infraction = {
  id: string
  controlType: ControlType
  infractionType?: InfractionTypeEnum
  natinfs?: string[]
  observations?: string
  target?: InfractionTarget
}

export type InfractionTarget = {
  id: string
  natinfs?: Natinf[]
  observations?: string
  companyName?: string
  relevantCourt?: string
  infractionType?: InfractionTypeEnum
  formalNotice?: FormalNoticeEnum
  toProcess?: Boolean
  vesselType?: VesselTypeEnum
  vesselSize?: VesselSizeEnum
  vesselIdentifier?: string
  identityControlledPerson?: string
  vehicleType: VehicleTypeEnum
}

export type InfractionByTarget = {
  vesselIdentifier: string
  vesselType: VesselTypeEnum
  infractions: Infraction[]
  controlTypesWithInfraction?: ControlType[]
  targetAddedByUnit?: boolean
  identityControlledPerson?: string
}

export type InfractionEnvNewTarget = Infraction & {
  controlType: ControlType
  identityControlledPerson: string
  vesselType: VesselTypeEnum
  vesselSize: VesselSizeEnum
  vesselIdentifier: string
}

export type Natinf = {
  infraction: string
  infractionCategory?: string
  natinfCode: string
  regulation?: string
}
