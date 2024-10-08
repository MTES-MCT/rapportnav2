import { FC } from 'react'
import { ActionTypeEnum } from '@common/types/env-mission-types.ts'
import ActionControlEnv from './action-control-env.tsx'
import { Action } from '@common/types/action-types.ts'
import { isEnvAction, isFishAction, isNavAction } from '../../../utils/action-utils.ts'
import ActionControlFish from './action-control-fish.tsx'
import ActionControlNav from './action-control-nav.tsx'
import ActionStatusForm from './action-status-form.tsx'
import ActionNoteForm from './action-note-form.tsx'
import ActionSurveillanceEnv from './action-surveillance-env.tsx'
import ActionRescueForm from './action-rescue-form.tsx'
import ActionNauticalEventForm from './action-nautical-event-form.tsx'
import ActionVigimerForm from './action-vigimer-form.tsx'
import ActionAntiPollutionForm from './action-anti-pollution-form.tsx'
import ActionBAAEMPermanenceForm from './action-baaem-permanence-form.tsx'
import ActionPublicOrderForm from './action-public-order-form.tsx'
import ActionRepresentationForm from './action-representation-form.tsx'
import ActionIllegalImmigrationForm from './action-illegal-immigration-form.tsx'

export interface ActionDetailsProps {
  action: Action
}

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
      case ActionTypeEnum.NAUTICAL_EVENT:
        return ActionNauticalEventForm
      case ActionTypeEnum.VIGIMER:
        return ActionVigimerForm
      case ActionTypeEnum.ANTI_POLLUTION:
        return ActionAntiPollutionForm
      case ActionTypeEnum.BAAEM_PERMANENCE:
        return ActionBAAEMPermanenceForm
      case ActionTypeEnum.PUBLIC_ORDER:
        return ActionPublicOrderForm
      case ActionTypeEnum.REPRESENTATION:
        return ActionRepresentationForm
      case ActionTypeEnum.ILLEGAL_IMMIGRATION:
        return ActionIllegalImmigrationForm
      case ActionTypeEnum.OTHER:
        return null
      default:
        return null
    }
  }

  return null
}
