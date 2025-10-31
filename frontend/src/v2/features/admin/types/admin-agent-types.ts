import { AdminService } from './admin-services-type'

export type Agent = {
  id?: string
  firstName?: string
  lastName?: string
  services?: string[]
  deletedAt?: Date
  createdAt?: Date
  updatedAt?: Date
}

export type AgentRole = {
  id: string
  title: string
}

export type AdminAgentService = {
  id: number
  agent: Agent
  role: AgentRole
  createdAt?: Date
  updatedAt?: Date
  disabledAt?: Date
}

export type AdminServiceWithAgent = {
  service: AdminService
  agentServices: AdminAgentService[]
}

export type AdminAgentServiceInput = {
  id?: number
  agentId: number
  serviceId: number
  roleId?: number
}
