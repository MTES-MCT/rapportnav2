export type ControlUnitResource = {
  id: string
  controlUnit: ControlUnit
  controlUnitId: number
  isArchived: boolean
  name: string
  note?: string
  photo?: string
  station: Station
  stationId: number
  type: ControlUnitResourceType
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

enum ControlUnitResourceType {
  AIRPLANE = "AIRPLANE",
  BARGE = "BARGE",
  CAR = "CAR",
  DRONE = "DRONE",
  EQUESTRIAN = "EQUESTRIAN",
  FAST_BOAT = "FAST_BOAT",
  FRIGATE = "FRIGATE",
  HELICOPTER = "HELICOPTER",
  HYDROGRAPHIC_SHIP = "HYDROGRAPHIC_SHIP",
  KAYAK = "KAYAK",
  LIGHT_FAST_BOAT = "LIGHT_FAST_BOAT",
  MINE_DIVER = "MINE_DIVER",
  MOTORCYCLE = "MOTORCYCLE",
  NET_LIFTER = "NET_LIFTER",
  NO_RESOURCE = "NO_RESOURCE",
  OTHER = "OTHER",
  PATROL_BOAT = "PATROL_BOAT",
  PEDESTRIAN = "PEDESTRIAN",
  PIROGUE = "PIROGUE",
  RIGID_HULL = "RIGID_HULL",
  SEA_SCOOTER = "SEA_SCOOTER",
  SEMI_RIGID = "SEMI_RIGID",
  SUPPORT_SHIP = "SUPPORT_SHIP",
  TRAINING_SHIP = "TRAINING_SHIP",
  TUGBOAT = "TUGBOAT"
}
