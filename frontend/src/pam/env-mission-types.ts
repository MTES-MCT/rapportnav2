import { compareAsc, compareDesc, parseISO } from '../dates'
import { ControlUnit } from './control-unit-types'

export enum ActionTypeEnum {
  CONTROL = 'CONTROL',
  NOTE = 'NOTE',
  SURVEILLANCE = 'SURVEILLANCE',
  STATUS = 'STATUS',
  CONTACT = 'CONTACT',
  RESCUE = 'RESCUE',
  OTHER = 'OTHER'
}
export const actionTypeLabels = {
  CONTROL: {
    code: 'CONTROL',
    libelle: 'Contrôle'
  },
  NOTE: {
    code: 'NOTE',
    libelle: 'Note'
  },
  SURVEILLANCE: {
    code: 'SURVEILLANCE',
    libelle: 'Surveillance'
  }
}

export enum MissionTypeEnum {
  AIR = 'AIR',
  LAND = 'LAND',
  SEA = 'SEA'
}
export const missionTypeEnum = {
  SEA: {
    code: 'SEA',
    libelle: 'Mer'
  },
  LAND: {
    code: 'LAND',
    libelle: 'Terre'
  },
  AIR: {
    code: 'AIR',
    libelle: 'Air'
  }
}

export enum InfractionTypeEnum {
  WAITING = 'WAITING',
  WITHOUT_REPORT = 'WITHOUT_REPORT',
  WITH_REPORT = 'WITH_REPORT'
}
export const infractionTypeLabels = {
  WITH_REPORT: {
    code: 'WITH_REPORT',
    libelle: 'Avec PV'
  },
  WITHOUT_REPORT: {
    code: 'WITHOUT_REPORT',
    libelle: 'Sans PV'
  },
  WAITING: {
    code: 'WAITING',
    libelle: 'En attente'
  }
}

export enum FormalNoticeEnum {
  NO = 'NO',
  PENDING = 'PENDING',
  YES = 'YES'
}
export const formalNoticeLabels = {
  YES: {
    code: 'YES',
    libelle: 'Oui'
  },
  NO: {
    code: 'NO',
    libelle: 'Non'
  },
  PENDING: {
    code: 'PENDING',
    libelle: 'En attente'
  }
}

export enum ActionTargetTypeEnum {
  COMPANY = 'COMPANY',
  INDIVIDUAL = 'INDIVIDUAL',
  VEHICLE = 'VEHICLE'
}
export const actionTargetTypeLabels = {
  COMPANY: {
    code: 'COMPANY',
    libelle: 'Société'
  },
  INDIVIDUAL: {
    code: 'INDIVIDUAL',
    libelle: 'Personne physique'
  },
  VEHICLE: {
    code: 'VEHICLE',
    libelle: 'Véhicule'
  }
}

export enum VehicleTypeEnum {
  OTHER_SEA = 'OTHER_SEA',
  VEHICLE_AIR = 'VEHICLE_AIR',
  VEHICLE_LAND = 'VEHICLE_LAND',
  VESSEL = 'VESSEL'
}

export const vehicleTypeLabels = {
  OTHER_SEA: {
    code: 'OTHER_SEA',
    libelle: 'Autre véhicule marin'
  },
  VEHICLE_AIR: {
    code: 'VEHICLE_AIR',
    libelle: 'Véhicule aérien'
  },
  VEHICLE_LAND: {
    code: 'VEHICLE_LAND',
    libelle: 'Véhicule terrestre'
  },
  VESSEL: {
    code: 'VESSEL',
    libelle: 'Navire'
  }
}

export const hasMissionOrderLabels = {
  YES: {
    label: 'Oui',
    value: true
  },
  NO: {
    label: 'Non',
    value: false
  }
}

export enum VesselTypeEnum {
  COMMERCIAL = 'COMMERCIAL',
  FISHING = 'FISHING',
  MOTOR = 'MOTOR',
  SAILING = 'SAILING'
}
export const vesselTypeLabels = {
  COMMERCIAL: {
    code: 'COMMERCIAL',
    libelle: 'Commerce'
  },
  FISHING: {
    code: 'FISHING',
    libelle: 'Pêche'
  },
  MOTOR: {
    code: 'MOTOR',
    libelle: 'Moteur'
  },
  SAILING: {
    code: 'SAILING',
    libelle: 'Voilier'
  }
}

export enum VesselSizeEnum {
  FROM_12_TO_24m = 'FROM_12_TO_24m',
  FROM_24_TO_46m = 'FROM_24_TO_46m',
  LESS_THAN_12m = 'LESS_THAN_12m',
  MORE_THAN_46m = 'MORE_THAN_46m'
}
export const vesselSizeEnum = {
  FROM_12_TO_24m: {
    code: 'FROM_12_TO_24m',
    libelle: '12 à 24 m'
  },
  FROM_24_TO_46m: {
    code: 'FROM_24_TO_46m',
    libelle: 'plus de 24 m'
  },
  LESS_THAN_12m: {
    code: 'LESS_THAN_12m',
    libelle: 'moins de 12 m'
  },
  MORE_THAN_46m: {
    code: 'MORE_THAN_46m',
    libelle: 'plus de 46 m'
  }
}

export const protectedSpeciesEnum = {
  BIRDS: {
    label: 'Oiseaux',
    value: 'BIRDS'
  },
  FLORA: {
    label: 'Flore',
    value: 'FLORA'
  },
  HABITAT: {
    label: 'Habitat',
    value: 'HABITAT'
  },
  MARINE_MAMMALS: {
    label: 'Mammifères marins',
    value: 'MARINE_MAMMALS'
  },
  OTHER: {
    label: 'Autres espèces protégées',
    value: 'OTHER'
  },
  REPTILES: {
    label: 'Reptiles',
    value: 'REPTILES'
  }
}

export enum MissionStatusEnum {
  CLOSED = 'CLOSED',
  ENDED = 'ENDED',
  PENDING = 'PENDING',
  UPCOMING = 'UPCOMING'
}
export const missionStatusLabels = {
  CLOSED: {
    code: 'CLOSED',
    libelle: 'Cloturée'
  },
  ENDED: {
    code: 'ENDED',
    libelle: 'Terminée'
  },
  PENDING: {
    code: 'PENDING',
    libelle: 'En cours'
  },
  UPCOMING: {
    code: 'UPCOMING',
    libelle: 'À venir'
  }
}

export enum MissionSourceEnum {
  MONITORENV = 'MONITORENV',
  MONITORFISH = 'MONITORFISH',
  POSEIDON_CACEM = 'POSEIDON_CACEM',
  POSEIDON_CNSP = 'POSEIDON_CNSP',
  RAPPORTNAV = 'RAPPORTNAV'
}

export const missionSourceEnum = {
  MONITORENV: {
    label: 'CACEM',
    value: 'MONITORENV'
  },
  MONITORFISH: {
    label: 'CNSP',
    value: 'MONITORFISH'
  }
}

export enum SeaFrontEnum {
  GUADELOUPE = 'Guadeloupe',
  GUYANE = 'Guyane',
  MARTINIQUE = 'Martinique',
  MAYOTTE = 'Mayotte',
  MED = 'MED',
  MEMN = 'MEMN',
  NAMO = 'NAMO',
  SA = 'SA',
  SOUTH_INDIAN_OCEAN = 'SOUTH_INDIAN_OCEAN'
}

export enum DateRangeEnum {
  CUSTOM = 'CUSTOM',
  DAY = 'DAY',
  MONTH = 'MONTH',
  WEEK = 'WEEK'
}

export const dateRangeLabels = {
  DAY: {
    label: 'Aujourd’hui',
    value: 'DAY'
  },
  WEEK: {
    label: 'Une semaine',
    value: 'WEEK'
  },
  MONTH: {
    label: 'Un mois',
    value: 'MONTH'
  },
  CUSTOM: {
    label: 'Période spécifique',
    value: 'CUSTOM'
  }
}

export const THEME_REQUIRE_PROTECTED_SPECIES = ['Police des espèces protégées et de leurs habitats (faune et flore)']

export const relevantCourtEnum = {
  JULIS: {
    code: 'JULIS',
    libelle: 'Juridictions littorales spécialisées (JULIS)'
  },
  LOCAL_COURT: {
    code: 'LOCAL_COURT',
    libelle: 'Parquet Local'
  },
  MARITIME_COURT: {
    code: 'MARITIME_COURT',
    libelle: 'Tribunal maritime'
  },
  PRE: {
    code: 'PRE',
    libelle: 'Pôle Régional Environnemental (PRE)'
  }
}

export type ResourceUnit = {
  administration: string
}

export type Mission<EnvAction = EnvActionControl | EnvActionSurveillance | EnvActionNote> = {
  id: any
  closedBy: string
  controlUnits: Omit<ControlUnit, 'id'>[]
  endDateTimeUtc?: string
  envActions: EnvAction[]
  facade: SeaFrontEnum
  geom?: Record<string, any>[]
  hasMissionOrder?: boolean
  isClosed: boolean
  isUnderJdp?: boolean
  missionSource: MissionSourceEnum
  missionTypes: MissionTypeEnum[]
  observationsCacem?: string
  observationsCnsp?: string
  openBy: string
  startDateTimeUtc: string
}

export type NewMission = Omit<Mission, 'facade' | 'missionSource'>

export type EnvAction = EnvActionControl | EnvActionSurveillance | EnvActionNote
export type NewEnvAction = NewEnvActionControl | EnvActionSurveillance | EnvActionNote

export type EnvActionCommonProperties = {
  actionEndDateTimeUtc?: string | null
  actionStartDateTimeUtc?: string | null
  geom?: Record<string, any>[]
  id: string
  actionStatus: string
}

export type EnvActionTheme = {
  protectedSpecies?: string[]
  subThemes?: string[]
  theme: string
}
export type NewEnvActionControl = EnvActionCommonProperties & {
  actionNumberOfControls?: number
  actionTargetType?: string
  actionType: ActionTypeEnum.CONTROL
  infractions: Infraction[]
  observations: string | null
  themes: EnvActionTheme[]
  vehicleType?: string
}
export type EnvActionControl = NewEnvActionControl & {
  actionTargetType: string
  amountOfControlsToComplete?: number
}

export type EnvActionSurveillance = EnvActionCommonProperties & {
  actionType: ActionTypeEnum.SURVEILLANCE
  coverMissionZone?: boolean
  durationMatchesMission?: boolean
  observations: string | null
  themes: EnvActionTheme[]
}

export type EnvActionNote = EnvActionCommonProperties & {
  actionType: ActionTypeEnum.NOTE
  observations?: string | null
}

export type NewInfraction = {
  companyName?: string | null
  controlledPersonIdentity?: string | null
  formalNotice?: FormalNoticeEnum
  id: string
  infractionType?: InfractionTypeEnum
  natinf?: string[]
  observations?: string | null
  registrationNumber?: string | null
  relevantCourt?: string | null
  toProcess: boolean
  vesselSize?: VesselSizeEnum | null
  vesselType?: VesselTypeEnum | null
}
export type Infraction = NewInfraction & {
  formalNotice: FormalNoticeEnum
  infractionType: InfractionTypeEnum
}

export const getMissionStatus = (mission: Mission, compareDate = Date.now()): MissionStatusEnum | string => {
  const { endDateTimeUtc, isClosed, startDateTimeUtc } = mission
  if (isClosed) {
    return MissionStatusEnum.CLOSED
  }
  if (startDateTimeUtc) {
    if (parseISO(startDateTimeUtc) && compareAsc(parseISO(startDateTimeUtc), compareDate) >= 0) {
      return MissionStatusEnum.UPCOMING
    }
    if (endDateTimeUtc && parseISO(endDateTimeUtc) && compareDesc(parseISO(endDateTimeUtc), compareDate) >= 0) {
      return MissionStatusEnum.ENDED
    }

    return MissionStatusEnum.PENDING
  }

  return 'ERROR'
}
