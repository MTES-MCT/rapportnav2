/**
 * Service
 * represents
 * ex: pam_X_bordée_A, ULAM33, GM
 */
export type Service = {
  id: string
  name: string
}

/**
 * Agent
 * represents an actual human part of a crew
 * it is linked to different services (mostly 1)
 */
export type Agent = {
  id: string
  firstName: string
  lastName: string
  services: Service[]
  deletedAt?: string
}

/**
 * AgentRole
 * represents the function of a member of a crew
 * such as captain, chief mecanic, ...
 */
export type AgentRole = {
  id: string
  title: string
}

export type AgentService = {
  agent: Agent
  role: AgentRole
}

export type ServiceWithAgents = {
  service: Service
  agents: AgentService[]
}
