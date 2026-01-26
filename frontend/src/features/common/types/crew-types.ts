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
}

/**
 * AgentRole
 * represents the function of a member of a crew
 * such as captain, chief mecanic, ...
 */
export type AgentRole = {
  id: string
  title: string
  disabledAt?: Date
  createdAt?: Date
  updatedAt?: Date
}

/**
 * MissionCrew
 * represents the actual crew of a mission
 * there will be as many rows as there are crew members of a mission
 */
export type MissionCrew = {
  id: string
  agent?: Agent
  comment?: string
  role?: AgentRole
}
