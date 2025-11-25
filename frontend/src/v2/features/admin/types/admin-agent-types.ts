import { RoleType } from '../../common/types/role-type'
import { AdminService } from './admin-services-type'

export type AdminAgent = {
  id?: number
  firstName: string
  lastName: string
  userId?: number
  role: AgentRole
  service: AdminService
  disabledAt?: Date
  createdAt?: Date
  updatedAt?: Date
}

export type AgentRole = {
  id: string
  title: string
}

export type AdminAgentInput = {
  id?: number
  serviceId: number
  roleId?: number
  firstName: string
  lastName: string
  userId?: number
}

export type AdminUserFromAgentInput = AdminUserInput & { agentId: number }

export interface AdminUserInput {
  id?: number
  email: string
  firstName?: string
  lastName?: string
  role: RoleType[]
  password?: string
  serviceId: number
}

export interface AdminUser {
  id: number
  email: string
  firstName?: string
  lastName?: string
  role: RoleType[]
  password?: string
  serviceId: number
  createdAt?: Date
  updatedAt?: Date
}
