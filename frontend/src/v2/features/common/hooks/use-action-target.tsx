import { ActionTargetTypeEnum } from '@common/types/env-mission-types'

type ActionTargetTypeRegistry = { [key in ActionTargetTypeEnum]: string }

const ACTION_TARGET_TYPE_REGISTRY: ActionTargetTypeRegistry = {
  [ActionTargetTypeEnum.VEHICLE]: 'Véhicule',
  [ActionTargetTypeEnum.COMPANY]: 'Personne morale',
  [ActionTargetTypeEnum.INDIVIDUAL]: 'Personne physique'
}

interface VesselHook {
  getActionTargetType: (type?: ActionTargetTypeEnum) => string | undefined
  vehiculeTypeOptions: { label: string; value: ActionTargetTypeEnum }[]
}

export function useActionTarget(): VesselHook {
  const getActionTargetType = (type?: ActionTargetTypeEnum) => (type ? ACTION_TARGET_TYPE_REGISTRY[type] : '')
  const getActionTargetTypeOptions = () =>
    Object.keys(ActionTargetTypeEnum)?.map(key => ({
      value: ActionTargetTypeEnum[key as keyof typeof ActionTargetTypeEnum],
      label: ACTION_TARGET_TYPE_REGISTRY[key as keyof typeof ActionTargetTypeEnum]
    }))
  return { getActionTargetType, vehiculeTypeOptions: getActionTargetTypeOptions() }
}
