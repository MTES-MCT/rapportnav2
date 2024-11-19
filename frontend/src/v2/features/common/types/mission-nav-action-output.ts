import { ActionStatusReason } from '@common/types/action-types'
import { ControlMethod } from '@common/types/control-types'
import { VesselSizeEnum, VesselTypeEnum } from '@common/types/env-mission-types'
import { MissionActionDataOutput, MissionActionOutput } from './mission-action-output'

export interface MissionNavActionDataOutput extends MissionActionDataOutput {
  startDateTimeUtc: string
  endDateTimeUtc: string
  observations: string
  latitude?: number
  longitude?: number
  detectedPollution: boolean
  pollutionObservedByAuthorizedAgent: boolean
  diversionCarriedOut: boolean
  isSimpleBrewingOperationDone: boolean
  isAntiPolDeviceDeployed: boolean
  controlMethod: ControlMethod
  vesselIdentifier: string
  vesselType: VesselTypeEnum
  vesselSize: VesselSizeEnum
  identityControlledPerson: string
  nbOfnumbererceptedVessels: number
  nbOfnumbererceptedMigrants: number
  nbOfSuspectedSmugglers: number
  isVesselRescue: boolean
  isPersonRescue: boolean
  isVesselNoticed: boolean
  isVesselTowed: boolean
  isInSRRorFollowedByCROSSMRCC: boolean
  numberPersonsRescued: number
  numberOfDeaths: number
  operationFollowsDEFREP: boolean
  locationDescription: string
  isMigrationRescue: boolean
  nbOfVesselsTrackedWithoutnumberervention: number
  nbAssistedVesselsReturningToShore: number
  reason: ActionStatusReason
}

export interface MissionNavActionOutput extends MissionActionOutput {
  data: MissionNavActionDataOutput
}
