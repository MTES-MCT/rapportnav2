export type MissionPassenger = {
  id?: string
  missionId?: number
  missionIdUUID?: string
  fullName: String
  organization?: string
  isIntern?: boolean
  startDateTimeUtc: string
  endDateTimeUtc: string
}
