import { FC } from 'react'
import { ActionTypeEnum } from '../../env-mission-types'
import ActionControlEnv from './action-control-env'
import { Action, isEnvAction, isFishAction, isNavAction } from '../../mission-types'
import ActionControlFish from './action-control-fish'
import { MissionActionType } from '../../fish-mission-types'
import ActionControlNav from './action-control-nav'

export const getComponentForAction = (action?: Action): FC<any> | null => {
  if (!action) {
    return null
  }
  if (isEnvAction(action)) {
    if (action.actionType === ActionTypeEnum.CONTROL) {
      return ActionControlEnv
    }
  } else if (isFishAction(action)) {
    if (
      [MissionActionType.SEA_CONTROL, MissionActionType.LAND_CONTROL, MissionActionType.AIR_CONTROL].indexOf(
        action.actionType as MissionActionType
      ) !== -1
    ) {
      return ActionControlFish
    }
  } else if (isNavAction(action)) {
    switch (action.actionType) {
      case ActionTypeEnum.CONTROL:
        return ActionControlNav
      case ActionTypeEnum.SURVEILLANCE:
        return null
      case ActionTypeEnum.STATUS:
        return null
      case ActionTypeEnum.NOTE:
        return null
      case ActionTypeEnum.CONTACT:
        return null
      case ActionTypeEnum.RESCUE:
        return null
      case ActionTypeEnum.OTHER:
        return null
      default:
        return null
    }
  }

  return null
}
