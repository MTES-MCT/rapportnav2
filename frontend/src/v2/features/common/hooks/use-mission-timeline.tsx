import { ActionTypeEnum, MissionSourceEnum } from '@common/types/env-mission-types'
import { MissionNavAction } from '../types/mission-action'

type ActionRegistryInput = { [key in ActionTypeEnum]?: unknown }
type Input = { missionId: number; startDateTimeUtc: Date }

const ACTION_REGISTRY_INPUT: ActionRegistryInput = {
  [ActionTypeEnum.NOTE]: { endDateTimeUtc: new Date().toISOString() },
  [ActionTypeEnum.RESCUE]: { isPersonRescue: false, isVesselRescue: true }
}

interface TimelineHook<T> {
  getActionInput: (actionType: ActionTypeEnum, moreData?: unknown) => MissionNavAction
}

export function useMissionTimeline<T>(missionId?: number): TimelineHook<T> {
  const getActionInput = (actionType: ActionTypeEnum, moreData?: unknown): MissionNavAction => {
    const input = {
      missionId: Number(missionId),
      actionType,
      source: MissionSourceEnum.RAPPORTNAV,
      data: {
        ...(moreData ?? {}),
        ...(ACTION_REGISTRY_INPUT[actionType] ?? {}),
        startDateTimeUtc: new Date().toISOString()
      }
    } as MissionNavAction

    return input
  }

  return { getActionInput }
}
