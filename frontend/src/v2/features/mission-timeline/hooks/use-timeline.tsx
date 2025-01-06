import { MissionSourceEnum } from '@common/types/env-mission-types'
import { MissionAction, MissionEnvAction, MissionFishAction, MissionNavAction } from '../../common/types/mission-action'
import { MissionTimelineAction } from '../types/mission-timeline-output'

interface TimelineHook {
  getTimeLineAction: (actions?: MissionAction[]) => MissionTimelineAction[]
  getTimeLineFromNavAction: (output: MissionAction) => MissionTimelineAction
  getTimeLineFromEnvAction: (output: MissionAction) => MissionTimelineAction
  getTimeLineFromFishAction: (output: MissionAction) => MissionTimelineAction
}

export function useTimeline(): TimelineHook {
  const getTimeLineAction = (actions?: MissionAction[]): MissionTimelineAction[] => {
    return (
      actions?.map(action => {
        switch (action.source) {
          case MissionSourceEnum.RAPPORTNAV:
            return getTimeLineFromNavAction(action)
          case MissionSourceEnum.MONITORENV:
            return getTimeLineFromEnvAction(action)
          case MissionSourceEnum.MONITORFISH:
            return getTimeLineFromFishAction(action)
          default:
            return {} as MissionTimelineAction
        }
      }) ?? []
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
      vehicleType: action.data?.vehicleType
    }
  }

  const getTimeLineFromNavAction = (output: MissionAction): MissionTimelineAction => {
    const action = output as MissionNavAction
    return {
      id: action.id,
      source: action.source,
      status: action.status,
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
      reason: action.data?.reason
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
      observations: action.data?.observationsByUnit
    }
  }

  return {
    getTimeLineAction,
    getTimeLineFromEnvAction,
    getTimeLineFromNavAction,
    getTimeLineFromFishAction
  }
}
