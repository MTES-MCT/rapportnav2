import { FC } from 'react'
import { ActionTypeEnum } from '../../env-mission-types'
import EnvActionControl from './env-action-control'
import { MissionAction } from '../../mission-types'
import FishActionControl from './fish-action-control'
import { MissionActionType } from '../../fish-mission-types'

export const getComponentForAction = (missionAction?: MissionAction): FC<any> | null => {
  if (!missionAction) return null
  switch (missionAction.actionType) {
    case ActionTypeEnum.CONTROL:
      return EnvActionControl
    case MissionActionType.SEA_CONTROL:
    case MissionActionType.LAND_CONTROL:
    case MissionActionType.AIR_CONTROL:
      return FishActionControl
    default:
      return null
  }
}
