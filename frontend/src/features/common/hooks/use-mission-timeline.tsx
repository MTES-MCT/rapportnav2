import { ActionTypeEnum } from '@common/types/env-mission-types'

type ActionRegistryInput = { [key in ActionTypeEnum]?: unknown }
type Input = { missionId: number; startDateTimeUtc: Date }

const ACTION_REGISTRY_INPUT: ActionRegistryInput = {
  [ActionTypeEnum.RESCUE]: { isPersonRescue: false, isVesselRescue: true }
}

interface TimelineHook<T> {
  getBaseInput: () => Input
  getActionDataInput: (actionType: ActionTypeEnum) => unknown
}

export function useMissionTimeline<T>(missionId?: string): TimelineHook<T> {
  const getActionDataInput = (actionType: ActionTypeEnum) => {
    const data = ACTION_REGISTRY_INPUT[actionType] || {}
    return {
      ...data,
      ...getBaseInput(),
      endDateTimeUtc: new Date()
    }
  }
  const getBaseInput = (): Input => ({ startDateTimeUtc: new Date(), missionId: parseInt(missionId!, 10) })

  return { getBaseInput, getActionDataInput }
}
