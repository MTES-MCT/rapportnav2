import { ActionStatusReason, ActionStatusType } from '@common/types/action-types'
import { ControlMethod, ControlType } from '@common/types/control-types'
import { ActionTargetTypeEnum, VehicleTypeEnum, VesselSizeEnum, VesselTypeEnum } from '@common/types/env-mission-types'
import {
  ControlCheck,
  FleetSegment,
  GearControl,
  GearInfraction,
  LogbookInfraction,
  MissionActionType,
  OtherInfraction,
  SpeciesControl,
  SpeciesInfraction
} from '@common/types/fish-mission-types'
import { InfractionByTarget } from '@common/types/infraction-types'
import { CompletenessForStats } from '@common/types/mission-types'
import { ActionType } from './action-type'
import { CrossControl } from './crossed-control-type.ts'
import { MissionActionData } from './mission-action-data'
import { MissionSource } from './mission-types'
import { NetworkSyncStatus } from './network-types.ts'

export interface MissionAction {
  id?: string
  missionId: number
  actionType: ActionType
  source: MissionSource
  status?: ActionStatusType
  summaryTags?: string[]
  isCompleteForStats?: boolean
  controlsToComplete: ControlType[]
  completenessForStats: CompletenessForStats
  sourcesOfMissingDataForStats: MissionSource[]
  networkSyncStatus?: NetworkSyncStatus
  data: MissionActionData
}

export interface MissionNavActionData extends MissionActionData {
  detectedPollution?: boolean
  pollutionObservedByAuthorizedAgent?: boolean
  diversionCarriedOut?: boolean
  isSimpleBrewingOperationDone?: boolean
  isAntiPolDeviceDeployed?: boolean
  controlMethod?: ControlMethod
  vesselIdentifier?: string
  vesselType?: VesselTypeEnum
  vesselSize?: VesselSizeEnum
  identityControlledPerson?: string
  nbOfnumbererceptedVessels?: number
  nbOfnumbererceptedMigrants?: number
  nbOfSuspectedSmugglers?: number
  isVesselRescue?: boolean
  isPersonRescue?: boolean
  isVesselNoticed?: boolean
  isVesselTowed?: boolean
  isInSRRorFollowedByCROSSMRCC?: boolean
  numberPersonsRescued?: number
  numberOfDeaths?: number
  operationFollowsDEFREP?: boolean
  locationDescription?: string
  isMigrationRescue?: boolean
  nbOfVesselsTrackedWithoutnumberervention?: number
  nbAssistedVesselsReturningToShore?: number
  reason?: ActionStatusReason
  crossControl?: CrossControl
}

export interface MissionNavAction extends MissionAction {
  data: MissionNavActionData
}

export interface MissionEnvActionData extends MissionActionData {
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
  infractions: InfractionByTarget[]
  coverMissionZone: Boolean
  formattedControlPlans: any
  controlsToComplete: ControlType[]
  availableControlTypesForInfraction: ControlType[]
}

export interface MissionEnvAction extends MissionAction {
  data: MissionEnvActionData
}

export interface MissionFishActionData extends MissionActionData {
  vesselId?: number
  vesselName?: string
  internalReferenceNumber?: string
  externalReferenceNumber?: string
  districtCode?: string
  faoAreas: string[]
  fishActionType: MissionActionType
  emitsVms?: ControlCheck
  emitsAis?: ControlCheck
  logbookMatchesActivity?: ControlCheck
  licencesMatchActivity?: ControlCheck
  speciesWeightControlled?: boolean
  speciesSizeControlled?: boolean
  separateStowageOfPreservedSpecies?: ControlCheck
  logbookInfractions?: LogbookInfraction[]
  licencesAndLogbookObservations?: string
  gearInfractions?: GearInfraction[]
  speciesInfractions?: SpeciesInfraction[]
  speciesObservations?: string
  seizureAndDiversion?: boolean
  otherInfractions?: OtherInfraction[]
  numberOfVesselsFlownOver?: number
  unitWithoutOmegaGauge?: boolean
  controlQualityComments?: string
  feedbackSheetRequired?: boolean
  userTrigram: string
  segments?: FleetSegment[]
  facade?: string
  longitude?: number
  latitude?: number
  portLocode?: string
  portName?: string
  vesselTargeted?: ControlCheck
  seizureAndDiversionComments?: string
  otherComments?: string
  gearOnboard?: GearControl[]
  speciesOnboard?: SpeciesControl[]
  isFromPoseidon?: boolean
  isDeleted?: boolean
  hasSomeGearsSeized?: boolean
  hasSomeSpeciesSeized?: boolean
  completedBy?: string
  completion?: string
  isAdministrativeControl?: boolean
  isComplianceWithWaterRegulationsControl?: boolean
  isSafetyEquipmentAndStandardsComplianceControl?: boolean
  isSeafarersControl?: boolean
  observationsByUnit?: string
  speciesQuantitySeized?: number
}

export interface MissionFishAction extends MissionAction {
  data: MissionFishActionData
}
