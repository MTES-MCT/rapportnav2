import React, { ReactNode } from 'react'
import styled from 'styled-components'

import { ActionTypeEnum } from '../../env-mission-types'
import { MissionActionType } from '../../fish-mission-types'
import { ActionSource } from '../../mission-types'

interface MissionTimelineItemContainerProps {
  actionType: ActionTypeEnum
  actionSource: ActionSource
  children: ReactNode
  componentMap?: Record<any, React.FC<{ children: any }>>
}

function createActionStyled(background?: string, border?: string, color?: string) {
  return styled.div`
    min-height: ${border ? '48px' : '52px'};
    background: ${background || 'inherit'} 0% 0% no-repeat padding-box;
    border: ${border ? `1px solid ${border}` : 'none'};
    color: ${color || '#282f3e'};
    font: normal normal medium 13px/18px Marianne;
    text-align: left;
    letter-spacing: 0px;
  `
}

const ActionControl = createActionStyled('#ffffff', '#cccfd6')
const ActionSurveillance = createActionStyled('#e5e5eb', '#cccfd6')
const ActionNote = createActionStyled('#d4dde7', '#cccfd6')
const ActionOther = createActionStyled('#d4e5f4', '#cccfd6')
const ActionStatus = createActionStyled(undefined, undefined, '#707785')
const ActionContact = createActionStyled(undefined, undefined, '#707785')

// const ActionComponentMap: Record<ActionTypeEnum | MissionActionType, React.FC<{ children: any }>> = {
//   [ActionTypeEnum.CONTROL]: ActionControl,
//   [ActionTypeEnum.SURVEILLANCE]: ActionSurveillance,
//   [ActionTypeEnum.NOTE]: ActionNote,
//   [ActionTypeEnum.CONTACT]: ActionContact,
//   [ActionTypeEnum.STATUS]: ActionStatus,
//   [ActionTypeEnum.OTHER]: ActionOther,
//   [MissionActionType.SEA_CONTROL]: ActionControl,
//   [MissionActionType.AIR_CONTROL]: ActionControl,
//   [MissionActionType.LAND_CONTROL]: ActionControl,
//   [MissionActionType.AIR_SURVEILLANCE]: ActionSurveillance,
//   [MissionActionType.OBSERVATION]: ActionOther
// }

const getActionComponent = (
  actionSource: ActionSource,
  actionType?: ActionTypeEnum | MissionActionType
): React.FC<{ children: any }> | null => {
  if (actionSource === ActionSource.EnvAction) {
    switch (actionType) {
      case ActionTypeEnum.CONTROL:
        return ActionControl
      default:
        return null
    }
  } else if (actionSource === ActionSource.FishAction) {
    switch (actionType) {
      case MissionActionType.SEA_CONTROL:
      case MissionActionType.AIR_CONTROL:
      case MissionActionType.LAND_CONTROL:
        return ActionControl
      default:
        return null
    }
  } else if (actionSource === ActionSource.NavAction) {
    return ActionControl
  }
  return null
}

const MissionTimelineItemContainer: React.FC<MissionTimelineItemContainerProps> = ({
  children,
  actionType,
  actionSource
}) => {
  const Component = getActionComponent(actionSource, actionType)

  if (!Component) {
    return null
  }

  return <Component>{children}</Component>
}

export default MissionTimelineItemContainer
