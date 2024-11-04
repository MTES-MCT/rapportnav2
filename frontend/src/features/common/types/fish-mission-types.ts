import { Undefine } from '@mtes-mct/monitor-ui'
import { Except } from 'type-fest'
import {
  ControlAdministrative,
  ControlGensDeMer,
  ControlNavigation,
  ControlSecurity,
  ControlType
} from './mission-types.ts'

export namespace LegacyControlUnit {
  export interface LegacyControlUnit {
    administration: string
    contact: string | undefined
    id: number
    isArchived: boolean
    name: string
    resources: LegacyControlUnitResource[]
  }

  export type LegacyControlUnitData = Except<LegacyControlUnit, 'id'>

  export type LegacyControlUnitDraft = Omit<Undefine<LegacyControlUnit>, 'resources'> &
    Pick<LegacyControlUnit, 'resources'>

  export interface LegacyControlUnitResource {
    id: number
    name: string
  }
}

export interface FishAction {
  actionDatetimeUtc: string
  actionEndDatetimeUtc?: string
  observationsByUnit?: string
  actionType: MissionActionType
  controlQualityComments: string | undefined
  controlUnits: LegacyControlUnit.LegacyControlUnit[]
  districtCode: string | undefined
  diversion: boolean | undefined
  emitsAis: ControlCheck | undefined
  emitsVms: ControlCheck | undefined
  externalReferenceNumber: string | undefined
  facade: string | undefined
  faoAreas: string[]
  feedbackSheetRequired: boolean | undefined
  flagState: string | undefined
  gearInfractions: GearInfraction[]
  gearOnboard: GearControl[]
  hasSomeGearsSeized: boolean
  hasSomeSpeciesSeized: boolean
  id: number
  internalReferenceNumber: string | undefined
  ircs: string | undefined
  isAdministrativeControl?: boolean
  isComplianceWithWaterRegulationsControl?: boolean
  isFromPoseidon: boolean | undefined
  isSafetyEquipmentAndStandardsComplianceControl?: boolean
  isSeafarersControl?: boolean
  isValid: boolean
  latitude: number | undefined
  licencesAndLogbookObservations: string | undefined
  licencesMatchActivity: ControlCheck | undefined
  logbookInfractions: LogbookInfraction[]
  logbookMatchesActivity: ControlCheck | undefined
  longitude: number | undefined
  missionId: number
  numberOfVesselsFlownOver: number | undefined
  otherComments: string | undefined
  otherInfractions: OtherInfraction[]
  portLocode: string | undefined
  // This field is added by the API
  portName: string | undefined
  segments: FleetSegment[]
  seizureAndDiversion: boolean | undefined
  seizureAndDiversionComments: string | undefined
  separateStowageOfPreservedSpecies: ControlCheck | undefined
  speciesInfractions: SpeciesInfraction[]
  speciesObservations: string | undefined
  speciesOnboard: SpeciesControl[]
  speciesSizeControlled: boolean | undefined
  speciesWeightControlled: boolean | undefined
  unitWithoutOmegaGauge: boolean | undefined
  userTrigram: string | undefined
  vesselId: number | undefined
  vesselName: string | undefined
  vesselTargeted: ControlCheck | undefined
  // Added by rapportnav:
  controlsToComplete?: ControlType[]
  controlAdministrative?: ControlAdministrative
  controlNavigation?: ControlNavigation
  controlSecurity?: ControlSecurity
  controlGensDeMer?: ControlGensDeMer
}

// ---------------------------------------------------------------------------
// Constants

export enum ControlCheck {
  NO = 'NO',
  NOT_APPLICABLE = 'NOT_APPLICABLE',
  YES = 'YES'
}

export enum FlightGoal {
  VMS_AIS_CHECK = 'VMS_AIS_CHECK',
  UNAUTHORIZED_FISHING = 'UNAUTHORIZED_FISHING',
  CLOSED_AREA = 'CLOSED_AREA'
}

export const FLIGHT_GOAL_LABEL: Record<FlightGoal, string> = {
  VMS_AIS_CHECK: 'Vérifications VMS/AIS',
  UNAUTHORIZED_FISHING: 'Pêche sans autorisation',
  CLOSED_AREA: 'Zones fermées'
}

export enum InfractionDomain {
  GEAR = 'GEAR',
  LOGBOOK = 'LOGBOOK',
  OTHER = 'OTHER',
  SPECIES = 'SPECIES'
}

export enum InfractionType {
  WITH_RECORD = 'WITH_RECORD',
  WITHOUT_RECORD = 'WITHOUT_RECORD',
  PENDING = 'PENDING'
}

export const INFRACTION_TYPE_LABEL: Record<InfractionType, string> = {
  [InfractionType.WITH_RECORD]: 'Avec PV',
  [InfractionType.WITHOUT_RECORD]: 'Sans PV',
  [InfractionType.PENDING]: 'En attente'
}

export enum MissionActionType {
  AIR_CONTROL = 'AIR_CONTROL',
  AIR_SURVEILLANCE = 'AIR_SURVEILLANCE',
  LAND_CONTROL = 'LAND_CONTROL',
  OBSERVATION = 'OBSERVATION',
  SEA_CONTROL = 'SEA_CONTROL'
}

export const formatMissionActionTypeForHumans = (type?: MissionActionType): string => {
  switch (type) {
    case MissionActionType.AIR_CONTROL:
      return 'Contrôle aérien'
    case MissionActionType.AIR_SURVEILLANCE:
      return 'Surveillance aérien'
    case MissionActionType.LAND_CONTROL:
      return 'Contrôle à terre'
    case MissionActionType.OBSERVATION:
      return 'Observation'
    case MissionActionType.SEA_CONTROL:
      return 'Contrôle en mer'
    default:
      return ''
  }
}

// ---------------------------------------------------------------------------
// Types

export type ControlAndText = {
  control: MissionAction | undefined
  text: string
}

export type FleetSegment = {
  segment: string | undefined
  segmentName: string | undefined
}

export type GearControl = {
  comments: string | undefined
  controlledMesh: number | undefined
  declaredMesh: number | undefined
  gearCode: string
  gearName: string
  gearWasControlled: boolean | undefined
  hasUncontrolledMesh: boolean
}

export type GearInfraction = {
  id?: string
  comments: string
  infractionType: InfractionType
  natinf: number
  natinfs?: string[]
  observations?: string
}

export type Infraction = {
  infraction: string
  infractionCategory: string
  natinfCode: number
  regulation: string
}

export type LastControls = {
  LAND: ControlAndText
  SEA: ControlAndText
}

export type LogbookInfraction = {
  id?: string
  comments: string
  infractionType: InfractionType
  // TODO This should be a plural.
  natinf: number
  natinfs?: string[]
  observations?: string
}

export type MissionActionData = Omit<MissionAction, 'id' | 'portName'> & {
  id: MissionAction['id'] | undefined
}

export type MissionControlsSummary = {
  controls: MissionAction[]
  numberOfControlsWithSomeGearsSeized: number
  numberOfControlsWithSomeSpeciesSeized: number
  numberOfDiversions: number
  vesselId: number
}

export type OtherInfraction = {
  id?: string
  comments: string
  infractionType: InfractionType
  natinf: number
  natinfs?: string[]
  observations?: string
}

export type SpeciesControl = {
  controlledWeight: number | undefined
  declaredWeight: number | undefined
  nbFish: number | undefined
  speciesCode: string
  underSized: boolean | undefined
}

export type SpeciesInfraction = {
  id?: string
  comments: string
  infractionType: InfractionType
  natinf: number
  natinfs?: string[]
  observations?: string
}
