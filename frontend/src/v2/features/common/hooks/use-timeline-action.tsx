import { UTCDate } from '@date-fns/utc'
import { IconProps, isNumeric } from '@mtes-mct/monitor-ui'
import { FunctionComponent } from 'react'
import { TimelineAction } from '../../mission-timeline/types/mission-timeline-output'
import { ActionType } from '../types/action-type'
import { MissionNavAction } from '../types/mission-action'
import { MissionSourceEnum } from '../types/mission-types'

export type ActionStyle = {
  color?: string
  minHeight?: number
  borderColor?: string
  backgroundColor?: string
}

export type ActionTimeline = {
  title?: string
  style: ActionStyle
  noPadding?: boolean
  type: ActionType
  component: FunctionComponent<{
    title?: string
    isSelected?: boolean
    action?: TimelineAction
    prevAction?: TimelineAction
    icon?: FunctionComponent<IconProps>
  }>
  icon?: FunctionComponent<IconProps>
}

type ActionRegistryInput = { [key in ActionType]?: unknown }

const ACTION_REGISTRY_INPUT: ActionRegistryInput = {
  [ActionType.NOTE]: { endDateTimeUtc: new UTCDate().toISOString() },
  [ActionType.RESCUE]: { isPersonRescue: false, isVesselRescue: true }
}

interface TimelineActionHook<T> {
  getActionInput: (actionType: ActionType, moreData?: unknown) => MissionNavAction
}

export function useTimelineAction<T>(id: string): TimelineActionHook<T> {
  const getActionInput = (actionType: ActionType, moreData?: unknown): MissionNavAction => {
    const input = {
      actionType,
      source: MissionSourceEnum.RAPPORTNAV,
      data: {
        ...(moreData ?? {}),
        ...(ACTION_REGISTRY_INPUT[actionType] ?? {}),
        startDateTimeUtc: new UTCDate().toISOString(),
        isWithinDepartment: true,
        hasDivingDuringOperation: false
      },
      ownerId: !isNumeric(id) ? id : undefined,
      missionId: isNumeric(id) ? Number(id) : undefined
    } as MissionNavAction

    return input
  }

  return { getActionInput }
}
