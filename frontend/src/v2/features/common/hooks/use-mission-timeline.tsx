import { ActionType } from '../types/action-type'
import { MissionNavAction } from '../types/mission-action'
import { MissionSourceEnum } from '../types/mission-types'
import { UTCDate } from '@date-fns/utc'

type ActionRegistryInput = { [key in ActionType]?: unknown }

const ACTION_REGISTRY_INPUT: ActionRegistryInput = {
  [ActionType.NOTE]: { endDateTimeUtc: new UTCDate().toISOString() },
  [ActionType.RESCUE]: { isPersonRescue: false, isVesselRescue: true }
}

interface TimelineHook<T> {
  getActionInput: (actionType: ActionType, moreData?: unknown) => MissionNavAction
}

export function useMissionTimeline<T>(missionId?: number): TimelineHook<T> {
  const getActionInput = (actionType: ActionType, moreData?: unknown): MissionNavAction => {
    const input = {
      missionId: Number(missionId),
      actionType,
      source: MissionSourceEnum.RAPPORTNAV,
      data: {
        ...(moreData ?? {}),
        ...(ACTION_REGISTRY_INPUT[actionType] ?? {}),
        startDateTimeUtc: new UTCDate().toISOString()
      }
    } as MissionNavAction

    return input
  }

  return { getActionInput }
}
