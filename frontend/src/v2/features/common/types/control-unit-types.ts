export enum ControlUnitResourceType {
  AIRPLANE = 'AIRPLANE',
  BARGE = 'BARGE',
  CAR = 'CAR',
  DRONE = 'DRONE',
  EQUESTRIAN = 'EQUESTRIAN',
  FAST_BOAT = 'FAST_BOAT',
  FRIGATE = 'FRIGATE',
  HELICOPTER = 'HELICOPTER',
  HYDROGRAPHIC_SHIP = 'HYDROGRAPHIC_SHIP',
  KAYAK = 'KAYAK',
  LIGHT_FAST_BOAT = 'LIGHT_FAST_BOAT',
  MINE_DIVER = 'MINE_DIVER',
  MOTORCYCLE = 'MOTORCYCLE',
  NET_LIFTER = 'NET_LIFTER',
  NO_RESOURCE = 'NO_RESOURCE',
  OTHER = 'OTHER',
  PATROL_BOAT = 'PATROL_BOAT',
  PEDESTRIAN = 'PEDESTRIAN',
  PIROGUE = 'PIROGUE',
  RIGID_HULL = 'RIGID_HULL',
  SEA_SCOOTER = 'SEA_SCOOTER',
  SEMI_RIGID = 'SEMI_RIGID',
  SUPPORT_SHIP = 'SUPPORT_SHIP',
  TRAINING_SHIP = 'TRAINING_SHIP',
  TUGBOAT = 'TUGBOAT'
}

export const ControlUnitResourceTypeLabel: Record<ControlUnitResourceType, string> = {
  [ControlUnitResourceType.AIRPLANE]: 'Avion',
  [ControlUnitResourceType.BARGE]: 'Barge',
  [ControlUnitResourceType.CAR]: 'Voiture',
  [ControlUnitResourceType.DRONE]: 'Drône',
  [ControlUnitResourceType.EQUESTRIAN]: 'Équestre',
  [ControlUnitResourceType.FAST_BOAT]: 'Vedette',
  [ControlUnitResourceType.FRIGATE]: 'Frégate',
  [ControlUnitResourceType.HELICOPTER]: 'Hélicoptère',
  [ControlUnitResourceType.HYDROGRAPHIC_SHIP]: 'Bâtiment hydrographique',
  [ControlUnitResourceType.KAYAK]: 'Kayak',
  [ControlUnitResourceType.LIGHT_FAST_BOAT]: 'Vedette légère',
  [ControlUnitResourceType.MINE_DIVER]: 'Plongeur démineur',
  [ControlUnitResourceType.MOTORCYCLE]: 'Moto',
  [ControlUnitResourceType.NET_LIFTER]: 'Remonte-filets',
  [ControlUnitResourceType.NO_RESOURCE]: 'Aucun moyen',
  [ControlUnitResourceType.OTHER]: 'Autre',
  [ControlUnitResourceType.PATROL_BOAT]: 'Patrouilleur',
  [ControlUnitResourceType.PEDESTRIAN]: 'Piéton',
  [ControlUnitResourceType.PIROGUE]: 'Pirogue',
  [ControlUnitResourceType.RIGID_HULL]: 'Coque rigide',
  [ControlUnitResourceType.SEA_SCOOTER]: 'Scooter de mer',
  [ControlUnitResourceType.SEMI_RIGID]: 'Semi-rigide',
  [ControlUnitResourceType.SUPPORT_SHIP]: 'Bâtiment de soutien',
  [ControlUnitResourceType.TRAINING_SHIP]: 'Bâtiment-école',
  [ControlUnitResourceType.TUGBOAT]: 'Remorqueur'
}

export type ControlUnitResource = {
  id?: number
  controlUnitId: number
  name?: string
  type: ControlUnitResourceType
  registrationId?: string
  radioFrequency?: string
}

export type Station = {
  id: number
  latitude: number
  longitude: number
  name: string
}

export type ControlUnit = {
  id: number
  areaNote?: string
  administrationId: number
  departmentAreaInseeCode?: string
  isArchived: boolean
  name: string
  termsNote?: string
}

export type Administration = {
  id: number
  name: string
  controlUnitIds: number[]
  isArchived: boolean
  controlUnits: ControlUnit[]
}

export type ResourceInput = {
  id: number
  name: string
  controlUnitId: number
  registrationId?: string
  radioFrequency?: string
}
