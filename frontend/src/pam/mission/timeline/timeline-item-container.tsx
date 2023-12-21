import React, { ReactNode } from 'react'
import styled from 'styled-components'
import { ActionTypeEnum, MissionSourceEnum } from '../../../types/env-mission-types'

interface MissionTimelineItemContainerProps {
  actionType: ActionTypeEnum
  actionSource: MissionSourceEnum
  children: ReactNode
}

type ItemOptions = {
  backgroundColor?: string
  borderColor?: string
  color?: string
  noMinHeight?: boolean
}

function createActionStyled(options: ItemOptions) {
  return styled.div`
    min-height: ${options.noMinHeight ? 0 : options.borderColor ? '48px' : '52px'};
    background: ${options.backgroundColor || 'inherit'} 0% 0% no-repeat padding-box;
    border: ${options.borderColor ? `1px solid ${options.borderColor}` : 'none'};
    text-align: left;
    letter-spacing: 0px;
  `
}

const ActionControl = createActionStyled({backgroundColor: '#ffffff', borderColor: '#cccfd6'})
const ActionSurveillance = createActionStyled({backgroundColor: '#e5e5eb', borderColor: '#cccfd6'})
const ActionNote = createActionStyled({backgroundColor: '#d4dde7', borderColor: '#cccfd6'})
const ActionOther = createActionStyled({backgroundColor: '#d4e5f4', borderColor: '#cccfd6'})
const ActionStatus = createActionStyled({
  backgroundColor: undefined,
  borderColor: undefined,
  noMinHeight: true
})
const ActionContact = createActionStyled({backgroundColor: undefined, borderColor: undefined, color: '#707785'})

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
