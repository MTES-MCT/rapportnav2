import React, { ReactNode } from 'react'
import styled from 'styled-components'
import { ActionTypeEnum, MissionSourceEnum } from '../../../../../../common/types/env-mission-types.ts'
import { THEME } from '@mtes-mct/monitor-ui'

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
    background: ${options.backgroundColor ?? 'inherit'} 0% 0% no-repeat padding-box;
    border: ${options.borderColor ? `1px solid ${options.borderColor}` : 'none'};
    text-align: left;
    letter-spacing: 0;
  `
}

const ActionControl = createActionStyled({
  backgroundColor: THEME.color.white,
  borderColor: THEME.color.lightGray
})
const ActionSurveillance = createActionStyled({
  backgroundColor: '#e5e5eb',
  borderColor: THEME.color.lightGray
})
const ActionNote = createActionStyled({
  backgroundColor: THEME.color.blueYonder25,
  borderColor: THEME.color.lightGray
})

const ActionStatus = createActionStyled({
  backgroundColor: undefined,
  borderColor: undefined,
  noMinHeight: true
})
const ActionContact = createActionStyled({
  backgroundColor: undefined,
  borderColor: undefined,
  color: '#707785'
})

const ActionRescue = createActionStyled({
  backgroundColor: THEME.color.goldenPoppy25,
  borderColor: THEME.color.blueYonder25
})

const ActionVigimer = createActionStyled({
  backgroundColor: THEME.color.blueGray25,
  borderColor: THEME.color.lightGray
})

const ActionNauticalEvent = createActionStyled({
  backgroundColor: THEME.color.blueGray25,
  borderColor: THEME.color.lightGray
})

const ActionPublicOrder = createActionStyled({
  backgroundColor: THEME.color.blueGray25,
  borderColor: THEME.color.lightGray
})

const ActionRepresentation = createActionStyled({
  backgroundColor: THEME.color.blueGray25,
  borderColor: THEME.color.lightGray
})

const ActionAntiPollution = createActionStyled({
  backgroundColor: THEME.color.blueGray25,
  borderColor: THEME.color.lightGray
})

const ActionBAAEMPermanence = createActionStyled({
  backgroundColor: THEME.color.blueGray25,
  borderColor: THEME.color.lightGray
})

const ActionIllegalImmigration = createActionStyled({
  backgroundColor: THEME.color.blueGray25,
  borderColor: THEME.color.lightGray
})

const getActionComponent = (
  actionSource: MissionSourceEnum,
  actionType?: ActionTypeEnum
): React.FC<{ children: any }> | null => {
  if (actionSource === MissionSourceEnum.MONITORENV) {
    switch (actionType) {
      case ActionTypeEnum.CONTROL:
        return ActionControl
      case ActionTypeEnum.SURVEILLANCE:
        return ActionSurveillance
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
      case ActionTypeEnum.NOTE:
        return ActionNote
      case ActionTypeEnum.VIGIMER:
        return ActionVigimer
      case ActionTypeEnum.NAUTICAL_EVENT:
        return ActionNauticalEvent
      case ActionTypeEnum.RESCUE:
        return ActionRescue
      case ActionTypeEnum.REPRESENTATION:
        return ActionRepresentation
      case ActionTypeEnum.PUBLIC_ORDER:
        return ActionPublicOrder
      case ActionTypeEnum.ANTI_POLLUTION:
        return ActionAntiPollution
      case ActionTypeEnum.BAAEM_PERMANENCE:
        return ActionBAAEMPermanence
      case ActionTypeEnum.ILLEGAL_IMMIGRATION:
        return ActionIllegalImmigration
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
