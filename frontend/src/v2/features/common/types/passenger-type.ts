export type MissionPassenger = {
  id?: string
  missionId?: string
  fullName: String
  organization?: string
  isIntern?: boolean
  startDate: string
  endDate: string
}

export const PASSENGER_OPTIONS = [
  { value: 'AFF_MAR', label: 'Affaires Maritimes' },
  { value: 'ESPMER', label: 'ESPMER' },
  { value: 'LYCEE', label: 'Lycées Maritimes' },
  { value: 'OTHER', label: 'Autres' }
]
