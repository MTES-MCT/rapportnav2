import React, { ReactNode } from 'react'
import styled from 'styled-components'
import { ActionTypeEnum, MissionSourceEnum } from '../../env-mission-types'

interface MissionTimelineItemContainerProps {
  actionType: ActionTypeEnum
  actionSource: MissionSourceEnum
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

const getActionComponent = (
  actionSource: MissionSourceEnum,
  actionType?: ActionTypeEnum
): React.FC<{ children: any }> | null => {
  if (actionSource === MissionSourceEnum.MONITORENV) {
    switch (actionType) {
      case ActionTypeEnum.CONTROL:
        return ActionControl
      default:
        return null
    }
  } else if (actionSource === MissionSourceEnum.MONITORFISH) {
    switch (actionType) {
      case ActionTypeEnum.CONTROL:
        return ActionControl
      default:
        return null
    }
  } else if (actionSource === MissionSourceEnum.RAPPORTNAV) {
    switch (actionType) {
      case ActionTypeEnum.CONTROL:
        return ActionControl
      case ActionTypeEnum.STATUS:
        return ActionStatus
      default:
        return null
    }
  } else {
    return null
  }
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
