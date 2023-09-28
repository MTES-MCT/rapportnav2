import { FC } from 'react'
import { ActionTypeEnum } from '../../env-mission-types'
import ActionControlEnv from './action-control-env'
import { Action, isEnvAction, isFishAction, isNavAction } from '../../mission-types'
import ActionControlFish from './action-control-fish'
import ActionControlNav from './action-control-nav'
import ActionStatusForm from './action-status-form'

export const getComponentForAction = (action?: Action): FC<any> | null => {
  if (!action) {
    return null
  }
  if (isEnvAction(action)) {
    if (action.type === ActionTypeEnum.CONTROL) {
      return ActionControlEnv
    }
  } else if (isFishAction(action)) {
    if (action.type === ActionTypeEnum.CONTROL) {
      return ActionControlFish
    }
  } else if (isNavAction(action)) {
    switch (action.type) {
      case ActionTypeEnum.CONTROL:
        return ActionControlNav
      case ActionTypeEnum.SURVEILLANCE:
        return null
      case ActionTypeEnum.STATUS:
        return ActionStatusForm
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
