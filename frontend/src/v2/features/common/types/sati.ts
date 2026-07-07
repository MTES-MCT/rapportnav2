import { PnoType } from './pno-type'

export interface Sati {
  id?: string
  module: string
  actionId: string
  jpe?: SatiJpe
  resource?: ControlResource
  vessel?: SatiVessel
  createdAt?: string
  updatedAt?: string
  startDatetimeUtc?: string
  endDatetimeUtc?: string
  principalInspector: SatiInspector
  otherInspectors?: SatiInspector[]
}

export interface SatiJpe {
  pnoId?: string
  portId?: string
  portName?: string
  tripNumber?: string
  lastStopDate?: string
  pnoType?: PnoType
}

export interface SatiVessel {
  id?: number
  jpe?: SatiJpe
  type?: string
  name?: string
  immat?: string
  imo?: string
  length?: number
  extRef?: string
  ircs?: string
  owner?: SatiParty
  flagState?: string
  operator?: SatiParty
  pnoType?: string
  tripNumber?: string
  agent?: SatiParty
  master?: SatiParty
  isMasterOwner: boolean
}

export interface SatiParty {
  id?: number
  partyType?: string
  comments?: string
  signature: boolean
  contact?: Contact
}

export interface Contact {
  id?: number
  fullName?: string
  firstName?: string
  lastName?: string
  nationality?: string
  email?: string
  phone?: string
  address?: Address
}

export interface Address {
  id?: number
  street?: string
  fullAddress?: string
  zipcode?: string
  town?: string
  country?: string
  lng?: number
  lat?: number
}

export interface SatiInspector {
  id?: number
  cardId?: string
  agentId?: number
  party?: SatiParty
  authorityType?: AuthorityType
  isOutOfUnit: boolean
}

export interface ControlResource {
  id?: number
  name?: string
  type?: ControlUnitResourceType
  registrationId?: string
  radioFrequency?: string
}

export enum AuthorityType {
  AECP = 'AECP',
  OTHERS = 'OTHERS',
  MEMBER_FR = 'MEMBER_FR'
}

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
