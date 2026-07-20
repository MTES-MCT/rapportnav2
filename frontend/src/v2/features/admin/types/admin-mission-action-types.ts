export type AdminMissionAction = {
  id: string
  ownerId?: string
  startDateTimeUtc: string
  endDateTimeUtc?: string
  actionType: string
  status?: string
  isCompleteForStats?: boolean
}
