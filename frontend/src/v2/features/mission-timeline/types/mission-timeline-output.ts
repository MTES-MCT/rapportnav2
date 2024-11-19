import { ActionStatusReason, ActionStatusType } from '@common/types/action-types'
import { ControlMethod, ControlType } from '@common/types/control-types'
import {
  ActionTargetTypeEnum,
  ActionTypeEnum,
  MissionSourceEnum,
  VehicleTypeEnum,
  VesselSizeEnum,
  VesselTypeEnum
} from '@common/types/env-mission-types'
import { MissionActionType } from '@common/types/fish-mission-types'
import { CompletenessForStats } from '@common/types/mission-types'

export type MissionTimelineAction = {
  id: string
  type: ActionTypeEnum
  missionId: number
  source?: MissionSourceEnum
  status?: ActionStatusType
  fishActionType?: MissionActionType
  isCompleteForStats?: boolean
  summaryTags?: string[]
  completenessForStats?: CompletenessForStats
  startDateTimeUtc?: string
  endDateTimeUtc?: string
  vesselId?: number
  vesselName?: string
  actionNumberOfControls?: number
  actionTargetType?: ActionTargetTypeEnum
  vehicleType?: VehicleTypeEnum
  controlsToComplete?: ControlType[]
  formattedControlPlans?: any[]
  observations?: string
  isPersonRescue?: Boolean
  isVesselRescue?: Boolean
  reason?: ActionStatusReason
  controlMethod?: ControlMethod
  vesselIdentifier?: string
  vesselType?: VesselTypeEnum
  vesselSize?: VesselSizeEnum
}
