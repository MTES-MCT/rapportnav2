import { ActionStatusReason, ActionStatusType } from '@common/types/action-types'
import { ControlMethod, ControlType } from '@common/types/control-types'
import { ActionTargetTypeEnum, VehicleTypeEnum, VesselSizeEnum, VesselTypeEnum } from '@common/types/env-mission-types'
import { MissionActionType } from '@common/types/fish-mission-types'
import { CompletenessForStats } from '@common/types/mission-types'
import { ActionType } from '../../common/types/action-type'
import { MissionSource } from '../../common/types/mission-types'
import { SyncStatus } from '../../common/types/network-types.ts'

export type MissionTimelineAction = {
  id?: string
  type: ActionType
  missionId: number
  source?: MissionSource
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
  syncStatus?: SyncStatus
}
