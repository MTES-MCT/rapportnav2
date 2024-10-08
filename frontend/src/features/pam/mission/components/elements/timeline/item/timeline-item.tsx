import React from 'react'
import { THEME } from '@mtes-mct/monitor-ui'
import { ActionTypeEnum, MissionSourceEnum } from '@common/types/env-mission-types.ts'
import { FlexboxGrid } from 'rsuite'
import { Action } from '@common/types/action-types.ts'
import ActionEnvControl from './timeline-item-control-env.tsx'
import ActionFishControl from './timeline-item-control-fish.tsx'
import ActionNavControl from './timeline-item-control-nav.tsx'
import ActionStatus from './timeline-item-status.tsx'
import ActionNote from './timeline-item-note.tsx'
import ActionEnvSurveillance from './timeline-item-surveillance.tsx'
import ActionOtherRescue from './timeline-item-rescue.tsx'
import ActionNauticalEvent from './timeline-item-nautical-event.tsx'
import ActionVigimer from './timeline-item-vigimer.tsx'
import ActionAntiPollution from './timeline-item-anti-pollution.tsx'
import ActionBaaemPermanence from './timeline-item-baaem-permanence.tsx'
import ActionRepresentation from './timeline-item-representation.tsx'
import ActionPublicOrder from './timeline-item-public-order.tsx'
import ActionIllegalImmigration from './timeline-item-illegal-immigration.tsx'

export interface MissionTimelineItemProps {
  action: Action
  previousActionWithSameType?: Action
  onClick: (action: Action) => void
}

export const TimelineItemWrapper: React.FC<{
  onClick: any
  children: any
  borderWhenSelected?: boolean
}> = ({ onClick, children, borderWhenSelected = null }) => {
  return (
    <FlexboxGrid
      onClick={onClick}
      style={{
        width: '100%',
        border: !!borderWhenSelected ? `3px solid ${THEME.color.blueGray}` : 'none',
        cursor: 'pointer'
      }}
      justify="start"
    >
      {children}
    </FlexboxGrid>
  )
}

const getActionComponent = (action: Action) => {
  if (action.source === MissionSourceEnum.MONITORENV) {
    if (action.type === ActionTypeEnum.CONTROL) {
      return ActionEnvControl
    }
    if (action.type === ActionTypeEnum.SURVEILLANCE) {
      return ActionEnvSurveillance
    }
  } else if (action.source === MissionSourceEnum.MONITORFISH) {
    if (action.type === ActionTypeEnum.CONTROL) {
      return ActionFishControl
    }
  } else if (action.source === MissionSourceEnum.RAPPORTNAV) {
    switch (action.type) {
      case ActionTypeEnum.CONTROL:
        return ActionNavControl
      case ActionTypeEnum.STATUS:
        return ActionStatus
      case ActionTypeEnum.NOTE:
        return ActionNote
      case ActionTypeEnum.RESCUE:
        return ActionOtherRescue
      case ActionTypeEnum.NAUTICAL_EVENT:
        return ActionNauticalEvent
      case ActionTypeEnum.VIGIMER:
        return ActionVigimer
      case ActionTypeEnum.ANTI_POLLUTION:
        return ActionAntiPollution
      case ActionTypeEnum.BAAEM_PERMANENCE:
        return ActionBaaemPermanence
      case ActionTypeEnum.REPRESENTATION:
        return ActionRepresentation
      case ActionTypeEnum.PUBLIC_ORDER:
        return ActionPublicOrder
      case ActionTypeEnum.ILLEGAL_IMMIGRATION:
        return ActionIllegalImmigration
      default:
        return null
    }
  }
  return null
}

const MissionTimelineItem: React.FC<MissionTimelineItemProps> = (props: MissionTimelineItemProps) => {
  const Component = getActionComponent(props.action)
  // const Component = componentMap[action.actionType]

  if (!Component) {
    return null
  }

  return <Component {...props} />
}

export default MissionTimelineItem
