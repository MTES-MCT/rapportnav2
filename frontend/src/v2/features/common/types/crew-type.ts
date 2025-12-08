import { Service } from '@common/types/crew-types.ts'

export type Agent = {
  id: number
  firstName: string
  lastName: string
  services: Service[]
}

export type AgentRole = {
  id: string
  title: string
}

export type MissionCrewMember = {
  id?: string
  agent?: Agent
  fullName?: string
  comment?: string
  role?: AgentRole
  missionId?: number
  absences: MissionCrewAbsence[]
}

export type MissionCrewAbsence = {
  id?: number
  startDate?: Date
  endDate?: Date
  isAbsentFullMission?: boolean
  reason?: string
}
