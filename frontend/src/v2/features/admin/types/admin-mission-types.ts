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
  createdAt?: string
  updatedAt?: string
}
