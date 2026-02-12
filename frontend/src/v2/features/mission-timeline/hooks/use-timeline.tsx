import { IconProps } from '@mtes-mct/monitor-ui'
import { orderBy } from 'lodash'
import { FunctionComponent } from 'react'
import { ActionGroupType, ActionType } from '../../common/types/action-type'
import { MissionAction, MissionEnvAction, MissionFishAction, MissionNavAction } from '../../common/types/mission-action'
import { MissionSourceEnum, CompletenessForStatsStatusEnum } from '../../common/types/mission-types'
import { MissionTimelineAction } from '../types/mission-timeline-output'

export type TimeLineStyle = {
  color?: string
  minHeight?: number
  borderColor?: string
  backgroundColor?: string
}

export type Timeline = {
  title?: string
  style: TimeLineStyle
  noPadding?: boolean
  component: FunctionComponent<{
    title?: string
    isSelected?: boolean
    action?: MissionTimelineAction
    icon?: FunctionComponent<IconProps>
    prevAction?: MissionTimelineAction
  }>
  icon?: FunctionComponent<IconProps>
}

export type TimelineRegistry = {
  [key in ActionType]?: Timeline
}

export type TimelineDropdownSubItem = {
  disabled?: boolean
  type: ActionType | ActionGroupType
  dropdownText?: string
  icon?: FunctionComponent<IconProps>
}
export type TimelineDropdownItem = TimelineDropdownSubItem & { subItems?: TimelineDropdownSubItem[] }

interface TimelineHook {
  isIncomplete: (action?: MissionTimelineAction) => boolean
  getTimeLineAction: (actions?: MissionAction[]) => MissionTimelineAction[]
  getTimeLineFromNavAction: (output: MissionAction) => MissionTimelineAction
  getTimeLineFromEnvAction: (output: MissionAction) => MissionTimelineAction
  getTimeLineFromFishAction: (output: MissionAction) => MissionTimelineAction
}

export function useTimeline(): TimelineHook {
  const getTimeLineAction = (actions?: MissionAction[]): MissionTimelineAction[] => {
    return orderBy(
      actions?.map(action => {
        switch (action.source) {
          case MissionSourceEnum.RAPPORT_NAV:
            return getTimeLineFromNavAction(action)
          case MissionSourceEnum.MONITORENV:
            return getTimeLineFromEnvAction(action)
          case MissionSourceEnum.MONITORFISH:
            return getTimeLineFromFishAction(action)
          default:
            return {} as MissionTimelineAction
        }
      }) ?? [],
      'startDateTimeUtc',
      'desc'
    )
  }

  const getTimeLineFromEnvAction = (output: MissionAction): MissionTimelineAction => {
    const action = output as MissionEnvAction
    return {
      id: action.id,
      status: action.status,
      source: action.source,
      type: action.actionType,
      missionId: action.missionId,
      summaryTags: action.summaryTags,
      controlsToComplete: output.controlsToComplete,
      completenessForStats: action.completenessForStats,
      startDateTimeUtc: action.data?.startDateTimeUtc,
      endDateTimeUtc: action.data?.endDateTimeUtc,
      observations: action.data?.observations,
      actionNumberOfControls: action.data?.actionNumberOfControls,
      actionTargetType: action.data?.actionTargetType,
      vehicleType: action.data?.vehicleType,
      formattedControlPlans: action.data?.formattedControlPlans,
      networkSyncStatus: action.networkSyncStatus,
      themes: action.data.themes
    }
  }

  const getTimeLineFromNavAction = (output: MissionAction): MissionTimelineAction => {
    const action = output as MissionNavAction
    return {
      id: action.id,
      source: action.source,
      status: action.status ?? action.data?.status,
      missionId: action.missionId,
      type: action.actionType,
      summaryTags: action.summaryTags,
      controlsToComplete: output.controlsToComplete,
      completenessForStats: action.completenessForStats,
      startDateTimeUtc: action.data?.startDateTimeUtc,
      endDateTimeUtc: action.data?.endDateTimeUtc,
      observations: action.data?.observations,
      controlMethod: action.data?.controlMethod,
      vesselIdentifier: action.data?.vesselIdentifier,
      vesselType: action.data?.vesselType,
      vesselSize: action.data?.vesselSize,
      isVesselRescue: action.data?.isVesselRescue,
      isPersonRescue: action.data?.isPersonRescue,
      reason: action.data?.reason,
      nbrOfHours: action.data.nbrOfHours,
      networkSyncStatus: action.networkSyncStatus,
      sectorType: action.data?.sectorType
    }
  }

  const getTimeLineFromFishAction = (output: MissionAction): MissionTimelineAction => {
    const action = output as MissionFishAction
    return {
      id: action.id,
      source: action.source,
      status: action.status,
      type: action.actionType,
      missionId: action.missionId,
      summaryTags: action.summaryTags,
      controlsToComplete: output.controlsToComplete,
      fishActionType: action.data.fishActionType,
      completenessForStats: action.completenessForStats,
      startDateTimeUtc: action.data?.startDateTimeUtc,
      endDateTimeUtc: action.data?.endDateTimeUtc,
      vesselId: action.data?.vesselId,
      vesselName: action.data?.vesselName,
      externalReferenceNumber: action.data?.externalReferenceNumber,
      flagState: action?.data?.flagState,
      observations: action.data?.observationsByUnit,
      networkSyncStatus: action.networkSyncStatus
    }
  }

  const isIncomplete = (action?: MissionTimelineAction) =>
    action?.completenessForStats?.status === CompletenessForStatsStatusEnum.INCOMPLETE

  return {
    isIncomplete,
    getTimeLineAction,
    getTimeLineFromEnvAction,
    getTimeLineFromNavAction,
    getTimeLineFromFishAction
  }
}
