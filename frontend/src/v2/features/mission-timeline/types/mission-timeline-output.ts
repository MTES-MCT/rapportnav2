import { ActionStatusReason, ActionStatusType } from '@common/types/action-types'
import { ControlMethod, ControlType } from '@common/types/control-types'
import { ActionTargetTypeEnum, VehicleTypeEnum, VesselSizeEnum, VesselTypeEnum } from '@common/types/env-mission-types'
import { MissionActionType } from '@common/types/fish-mission-types'
import { CompletenessForStats } from '@common/types/mission-types'
import { ActionType } from '../../common/types/action-type'
import { MissionSource } from '../../common/types/mission-types'
import { NetworkSyncStatus } from '../../common/types/network-types.ts'
import { EnvTheme } from '@common/types/env-themes.ts'
import { SectorType } from '../../common/types/sector-types.ts'

export type MissionTimelineAction = {
  id?: string
  type: ActionType
  missionId?: string
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
  externalReferenceNumber?: string
  flagState?: string
  actionNumberOfControls?: number
  actionTargetType?: ActionTargetTypeEnum
  vehicleType?: VehicleTypeEnum
  controlsToComplete?: ControlType[]
  observations?: string
  isPersonRescue?: Boolean
  isVesselRescue?: Boolean
  reason?: ActionStatusReason
  controlMethod?: ControlMethod
  vesselIdentifier?: string
  vesselType?: VesselTypeEnum
  vesselSize?: VesselSizeEnum
  networkSyncStatus?: NetworkSyncStatus
  nbrOfHours?: number
  themes?: EnvTheme[]
  sectorType?: SectorType
}

export type TimelineAction = MissionTimelineAction
