import { ActionTypeEnum } from '@common/types/env-mission-types'
import { ActionRegistryHook, ActionRegistryItem, useActionRegistry } from '../../common/hooks/use-action-registry'

type UlamActionRegistry = {
  [key in ActionTypeEnum]?: ActionRegistryItem
}
const ULAM_ACTION_REGISTRY: UlamActionRegistry = {}

type UlamActionRegistryHook = {} & UlamActionRegistry & ActionRegistryHook

export function useUlamActionRegistry(actionType: ActionTypeEnum): UlamActionRegistryHook {
  const common = useActionRegistry(actionType)
  const ulam = ULAM_ACTION_REGISTRY[actionType]
  return { ...ulam, ...common }
}
