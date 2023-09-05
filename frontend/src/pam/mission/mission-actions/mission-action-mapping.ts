import { FC } from 'react'
import { ActionTypeEnum, EnvAction } from '../../mission-types'
import EnvActionControl from './env-action-control'

export const getComponentForAction = (missionAction?: EnvAction): FC<any> | null => {
  if (!missionAction) return null
  switch (missionAction.actionType) {
    case ActionTypeEnum.CONTROL:
      return EnvActionControl
    default:
      return null
  }
}
