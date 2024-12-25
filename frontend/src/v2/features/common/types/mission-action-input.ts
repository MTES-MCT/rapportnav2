import { ActionTypeEnum, MissionSourceEnum } from '@common/types/env-mission-types'
import { MissionEnvActionDataInput } from './mission-env-action-input'
import { MissionFishActionDataInput } from './mission-fish-action-input'
import { MissionNavActionDataInput } from './mission-nav-action-input'

export interface MissionActionInput {
  id: string
  missionId: number
  source: MissionSourceEnum
  actionType: ActionTypeEnum
  env?: MissionEnvActionDataInput
  nav?: MissionNavActionDataInput
  fish?: MissionFishActionDataInput
}
