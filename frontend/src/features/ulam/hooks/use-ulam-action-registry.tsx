import { ActionTypeEnum } from '@common/types/env-mission-types'
import { ActionRegistryItem, useActionRegistry } from '@features/v2/common/hooks/use-action-registry'

type UlamActionRegistry = {
  [key in ActionTypeEnum]?: ActionRegistryItem
}
const ULAM_ACTION_REGISTRY: UlamActionRegistry = {}

export function useUlamActionRegistry(actionType: ActionTypeEnum): ActionRegistryItem {
  const common = useActionRegistry(actionType)
  const ulam = ULAM_ACTION_REGISTRY[actionType]
  return { ...(ulam || common) }
}
