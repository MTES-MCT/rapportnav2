import { Service } from '@common/types/crew-types.ts'

/**
 * Agent
 * represents an actual human part of a crew
 * it is linked to different services (mostly 1)
 */
export type Agent = {
  id: number
  firstName: string
  lastName: string
  services: Service[]
}

/**
 * AgentRole
 * represents the function of a member of a crew
 * such as captain, chief mechanic, ...
 */
export type AgentRole = {
  id: string
  title: string
}

/**
 * MissionCrew
 * represents the actual crew of a mission
 * there will be as many rows as there are crew members of a mission
 */
export type MissionCrew = {
  id: string
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
  reason?: MissionCrewAbsenceReason
}

export enum MissionCrewAbsenceType {
  TEMPORARY = 'TEMPORARY',
  FULL_MISSION = 'FULL_MISSION'
}

export enum MissionCrewAbsenceReason {
  SICK_LEAVE = 'SICK_LEAVE',
  TRAINING = 'TRAINING',
  RECOVERING = 'RECOVERING',
  HOLIDAYS = 'HOLIDAYS',
  MEETING = 'MEETING',
  DISPATCHED_ELSEWHERE = 'DISPATCHED_ELSEWHERE',
  OTHER = 'OTHER'
}

export type AddOrUpdateMissionCrewInput = {
  id?: string
  fullName?: string
  comment?: string
  role?: AgentRole
  missionId: number
}
