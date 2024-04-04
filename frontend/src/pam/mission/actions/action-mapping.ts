import { FC } from 'react'
import { ActionTypeEnum } from '../../../types/env-mission-types'
import ActionControlEnv from './action-control-env'
import { Action } from '../../../types/action-types'
import { isEnvAction, isFishAction, isNavAction } from './utils'
import ActionControlFish from './action-control-fish'
import ActionControlNav from './action-control-nav'
import ActionStatusForm from './action-status-form'
import ActionNoteForm from "./action-note-form.tsx";
import ActionSurveillanceEnv from './action-surveillance-env.tsx'
import ActionRescueForm from './action-rescue-form.tsx'

export const getComponentForAction = (action?: Action): FC<any> | null => {
  if (!action) {
    return null
  }
  if (isEnvAction(action)) {
    if (action.type === ActionTypeEnum.CONTROL) {
      return ActionControlEnv
    }
    if (action.type === ActionTypeEnum.SURVEILLANCE) {
      return ActionSurveillanceEnv
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
        return ActionNoteForm
      case ActionTypeEnum.CONTACT:
        return null
      case ActionTypeEnum.RESCUE:
        return ActionRescueForm
      case ActionTypeEnum.OTHER:
        return null
      default:
        return null
    }
  }

  return null
}
