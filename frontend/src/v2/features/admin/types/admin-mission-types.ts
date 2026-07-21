export type AdminMission = {
  id: string
  serviceId?: number
  openBy?: string
  completedBy?: string
  externalId?: string
  startDateTimeUtc: string
  endDateTimeUtc?: string
  missionSource?: string
  isDeleted: boolean
  isCompleteForStats?: boolean
  sourcesOfMissingData?: string
  createdAt?: string
  updatedAt?: string
}
