import { PnoType } from './pno-type'

export enum SatiPartyType {
  VESSEL_AGENT = 'VESSEL_AGENT',
  VESSEL_MASTER = 'VESSEL_MASTER',
  VESSEL_OWNER = 'VESSEL_OWNER',
  VESSEL_OPERATOR = 'VESSEL_OPERATOR',
  INSPECTOR = 'INSPECTOR',
  VESSEL_BENEFICIARY = 'VESSEL_BENEFICIARY'
}

export enum SatiModuleType {
  M1 = 'M1',
  M3 = 'M3',
  M5 = 'M5',
  M6 = 'M6',
  M7 = 'M7'
}

export interface Sati {
  id?: string
  module: SatiModuleType
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
  beneficiary?: SatiParty
  isMasterOwner?: boolean
}

export interface SatiParty {
  id?: number
  partyType?: string
  comments?: string
  signature?: boolean
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
  MEMBER_FR = 'MEMBER_FR',
  AECP = 'AECP',
  OTHERS = 'OTHERS'
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
