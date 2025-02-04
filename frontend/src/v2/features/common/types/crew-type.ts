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

export type MissionCrew = {
  id?: string
  agent?: Agent
  comment?: string
  role?: AgentRole
  missionId?: number
}
