export enum ControlCheck {
  NO = 'NO',
  NOT_APPLICABLE = 'NOT_APPLICABLE',
  YES = 'YES'
}

export enum InfractionType {
  WITH_RECORD = 'WITH_RECORD',
  WITHOUT_RECORD = 'WITHOUT_RECORD',
  PENDING = 'PENDING'
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

export type Infraction = {
  infractionType?: InfractionType
  natinf?: number
  natinfDescription?: string
  threat?: string
  threatCharacterization?: string
  comments?: string
}

export type SpeciesControl = {
  controlledWeight: number | undefined
  declaredWeight: number | undefined
  nbFish: number | undefined
  speciesCode: string
  speciesName: string | undefined
  underSized: boolean | undefined
}
