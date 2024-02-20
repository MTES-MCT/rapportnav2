import React from 'react'
import { THEME } from '@mtes-mct/monitor-ui'
import { ActionTypeEnum, MissionSourceEnum } from '../../../../types/env-mission-types.ts'
import { FlexboxGrid } from 'rsuite'
import { Action } from '../../../../types/action-types.ts'
import ActionEnvControl from "./timeline-item-control-env.tsx";
import ActionFishControl from "./timeline-item-control-fish.tsx";
import ActionNavControl from "./timeline-item-control-nav.tsx";
import ActionStatus from "./timeline-item-status.tsx";
import ActionNote from "./timeline-item-note.tsx";

export interface MissionTimelineItemProps {
    action: Action
    onClick: (action: Action) => void
}

export const TimelineItemWrapper: React.FC<{
    onClick: any;
    children: any;
    borderWhenSelected?: boolean
}> = ({
          onClick,
          children,
          borderWhenSelected = null
      }) => {
    return (
        <div onClick={onClick}>
            <FlexboxGrid
                style={{
                    width: '100%',
                    border: !!borderWhenSelected ? `3px solid ${THEME.color.blueGray}` : 'none',
                    cursor: 'pointer'
                }}
                justify="start"
            >
                {children}
            </FlexboxGrid>
        </div>
    )
}


const getActionComponent = (action: Action) => {
    if (action.source === MissionSourceEnum.MONITORENV) {
        if (action.type === ActionTypeEnum.CONTROL) {
            return ActionEnvControl
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
            default:
                return null
        }
    }
    return null
}

const MissionTimelineItem: React.FC<MissionTimelineItemProps> = ({
                                                                     action,
                                                                     onClick
                                                                     // componentMap = ActionComponentMap
                                                                 }) => {
    const Component = getActionComponent(action)
    // const Component = componentMap[action.actionType]

    if (!Component) {
        return null
    }

    return <Component action={action as any} onClick={onClick}/>
}

export default MissionTimelineItem
