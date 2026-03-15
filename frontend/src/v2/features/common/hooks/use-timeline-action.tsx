import { IconProps, isNumeric } from '@mtes-mct/monitor-ui'
import { FunctionComponent } from 'react'
import { TimelineAction } from '../../mission-timeline/types/mission-timeline-output'
import { ActionType } from '../types/action-type'
import { MissionNavAction } from '../types/mission-action'
import { MissionSourceEnum } from '../types/mission-types'
import { MissionDates, useMissionDates } from './use-mission-dates.tsx'

const getActionStartDate = (missionDates: MissionDates): string | undefined => {
  const now = new Date()
  const start = missionDates.startDateTimeUtc ? new Date(missionDates.startDateTimeUtc) : undefined
  const end = missionDates.endDateTimeUtc ? new Date(missionDates.endDateTimeUtc) : undefined

  const isAfterStart = !start || now >= start
  const isBeforeEnd = !end || now <= end

  if (isAfterStart && isBeforeEnd) {
    return now.toISOString()
  }

  return missionDates.endDateTimeUtc
}

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
  [ActionType.RESCUE]: { isPersonRescue: false, isVesselRescue: true }
}

interface TimelineActionHook<T> {
  getActionInput: (actionType: ActionType, moreData?: unknown) => MissionNavAction
}

export function useTimelineAction<T>(id: string, type: 'mission' | 'inquiry' = 'mission'): TimelineActionHook<T> {
  const missionDates = useMissionDates(id, type)

  const getActionInput = (actionType: ActionType, moreData?: unknown): MissionNavAction => {
    const input = {
      actionType,
      source: MissionSourceEnum.RAPPORT_NAV,
      data: {
        ...(moreData ?? {}),
        ...(ACTION_REGISTRY_INPUT[actionType] ?? {}),
        startDateTimeUtc: getActionStartDate(missionDates), // use today if within mission range, otherwise mission end date
        isWithinDepartment: true
      },
      ownerId: !isNumeric(id) ? id : undefined,
      missionId: isNumeric(id) ? Number(id) : undefined
    } as MissionNavAction

    return input
  }

  return { getActionInput }
}
