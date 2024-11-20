import { ControlType } from '@common/types/control-types'
import { ActionTargetTypeEnum, VehicleTypeEnum } from '@common/types/env-mission-types'
import { InfractionByTarget } from '@common/types/infraction-types'
import { MissionActionDataOutput, MissionActionOutput } from './mission-action-output'

export interface MissionEnvActionDataOutput extends MissionActionDataOutput {
  completedBy: string
  geom: string
  facade: string
  department: string
  isAdministrativeControl: Boolean
  isComplianceWithWaterRegulationsControl: Boolean
  isSafetyEquipmentAndStandardsComplianceControl: Boolean
  isSeafarersControl: Boolean
  openBy: string
  observations: string
  observationsByUnit: string
  actionNumberOfControls: number
  actionTargetType: ActionTargetTypeEnum
  vehicleType: VehicleTypeEnum
  infractions: InfractionByTarget[][]
  coverMissionZone: Boolean
  formattedControlPlans: any
  controlsToComplete: ControlType[]
  availableControlTypesForInfraction: ControlType[]
}

export interface MissionEnvActionOutput extends MissionActionOutput {
  data: MissionEnvActionDataOutput
}
