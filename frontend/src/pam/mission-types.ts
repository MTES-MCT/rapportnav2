import { act } from 'react-dom/test-utils'
import { ControlUnit } from './control-unit-types'
import { EnvAction, MissionSourceEnum, MissionTypeEnum, SeaFrontEnum } from './env-mission-types'
import { FishAction } from './fish-mission-types'

export type RapportNavAction = {
  id: number
  actionType: any
  actionEndDateTimeUtc?: string | null
  actionStartDateTimeUtc?: string | null
}
export type MissionActions = {
  envAction?: EnvAction
  fishAction?: FishAction
  rapportNavAction?: RapportNavAction
}
export type MissionAction = EnvAction | FishAction | RapportNavAction

export type Mission = {
  id: number
  closedBy: string
  controlUnits: Omit<ControlUnit, 'id'>[]
  endDateTimeUtc?: string
  envActions: EnvAction[]
  facade: SeaFrontEnum
  geom?: Record<string, any>[]
  hasMissionOrder?: boolean
  isClosed: boolean
  isUnderJdp?: boolean
  missionSource: MissionSourceEnum
  missionTypes: MissionTypeEnum[]
  observationsCacem?: string
  observationsCnsp?: string
  openBy: string
  startDateTimeUtc: string
  actions: MissionActions[]
}

export enum ControlTarget {
  'PECHE_PRO' = 'PECHE_PRO',
  'PLAISANCE_PRO' = 'PLAISANCE_PRO',
  'COMMERCE_PRO' = 'COMMERCE_PRO',
  'SERVICE_PRO' = 'SERVICE_PRO',
  'PLAISANCE_LOISIR' = 'PLAISANCE_LOISIR'
}

export const getActionData = (action: MissionActions): EnvAction | FishAction | RapportNavAction => {
  if (action.envAction) {
    return action.envAction as EnvAction
  } else if (action.fishAction) {
    return action.fishAction as FishAction
  }

  return action.rapportNavAction as RapportNavAction
}

export const getActionStartTime = (action: MissionActions): string | undefined | null => {
  if (action.envAction) {
    return action.envAction.actionStartDateTimeUtc
  } else if (action.fishAction) {
    return action.fishAction.actionDatetimeUtc
  }

  return action.rapportNavAction?.actionStartDateTimeUtc
}
