import { ActionRegistryHook, ActionRegistryItem, useActionRegistry } from '../../common/hooks/use-action-registry'
import { ActionType } from '../../common/types/action-type'

type UlamActionRegistry = {
  [key in ActionType]?: ActionRegistryItem
}

const ULAM_ACTION_REGISTRY: UlamActionRegistry = {}

type UlamActionRegistryHook = {} & UlamActionRegistry & ActionRegistryHook

export function useUlamActionRegistry(actionType: ActionType): UlamActionRegistryHook {
  const common = useActionRegistry(actionType)
  const ulam = ULAM_ACTION_REGISTRY[actionType]
  return { ...ulam, ...common }
}
