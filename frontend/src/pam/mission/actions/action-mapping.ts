import { FC } from 'react'
import { ActionTypeEnum } from '../../env-mission-types'
import ActionControlEnv from './action-control-env'
import { MissionAction, isEnvAction, isFishAction, isNavAction } from '../../mission-types'
import ActionControlFish from './action-control-fish'
import { MissionActionType } from '../../fish-mission-types'
import ActionControlNav from './action-control-nav'

export const getComponentForAction = (missionAction?: MissionAction): FC<any> | null => {
  if (!missionAction) {
    return null
  }
  if (isEnvAction(missionAction)) {
    if (missionAction.actionType === ActionTypeEnum.CONTROL) {
      return ActionControlEnv
    }
  } else if (isFishAction(missionAction)) {
    if (
      [MissionActionType.SEA_CONTROL, MissionActionType.LAND_CONTROL, MissionActionType.AIR_CONTROL].indexOf(
        missionAction.actionType
      ) !== -1
    ) {
      return ActionControlFish
    }
  } else if (isNavAction(missionAction)) {
    switch (missionAction.actionType) {
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
