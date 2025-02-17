export type ControlUnitResource = {
  id?: number
  controlUnitId?: number
  name: string
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
