import { ControlType } from '@common/types/control-types'
import { FormalNoticeEnum, InfractionTypeEnum, VesselSizeEnum, VesselTypeEnum } from '@common/types/env-mission-types'

export interface InfractionEnvTargetOutput {
  vesselIdentifier: string
  companyName: string
  relevantCourt: string
  formalNotice: FormalNoticeEnum
  toProcess: boolean
  identityControlledPerson: string
  vesselType: VesselTypeEnum
  vesselSize: VesselSizeEnum
}

export interface InfractionEnvDataOutput {
  id?: string
  missionId?: number
  actionId?: string
  controlId?: string
  controlType?: ControlType
  infractionType?: InfractionTypeEnum
  observations?: string
  natinfs?: string[]
  target?: InfractionEnvTargetOutput
}
