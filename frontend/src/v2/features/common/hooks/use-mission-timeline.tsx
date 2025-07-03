import { UTCDate } from '@date-fns/utc'
import { isNumeric } from '@mtes-mct/monitor-ui'
import { ActionType } from '../types/action-type'
import { MissionNavAction } from '../types/mission-action'
import { MissionSourceEnum } from '../types/mission-types'

type ActionRegistryInput = { [key in ActionType]?: unknown }

const ACTION_REGISTRY_INPUT: ActionRegistryInput = {
  [ActionType.NOTE]: { endDateTimeUtc: new UTCDate().toISOString() },
  [ActionType.RESCUE]: { isPersonRescue: false, isVesselRescue: true }
}

interface TimelineHook<T> {
  getActionInput: (actionType: ActionType, moreData?: unknown) => MissionNavAction
}

export function useMissionTimeline<T>(missionId?: string): TimelineHook<T> {
  const getActionInput = (actionType: ActionType, moreData?: unknown): MissionNavAction => {
    const input = {
      actionType,
      source: MissionSourceEnum.RAPPORTNAV,
      data: {
        ...(moreData ?? {}),
        ...(ACTION_REGISTRY_INPUT[actionType] ?? {}),
        startDateTimeUtc: new UTCDate().toISOString()
      },
      ownerId: !isNumeric(missionId) ? missionId : undefined,
      missionId: isNumeric(missionId) ? Number(missionId) : undefined
    } as MissionNavAction

    return input
  }

  return { getActionInput }
}
