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
import { MissionActionDataOutput, MissionActionOutput } from './mission-action-output'

export interface MissionFishActionDataOutput extends MissionActionDataOutput {
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

export interface MissionFishActionOutput extends MissionActionOutput {
  data: MissionFishActionDataOutput
}
