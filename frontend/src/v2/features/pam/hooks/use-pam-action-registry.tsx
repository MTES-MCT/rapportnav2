import { ActionRegistryHook, ActionRegistryItem, useActionRegistry } from '../../common/hooks/use-action-registry'
import { ActionType } from '../../common/types/action-type'
import MissionActionItemStatus from '../../mission-action/components/elements/mission-action-item-status'

type PamActionRegistry = {
  [key in ActionType]?: ActionRegistryItem
}

const PAM_ACTION_REGISTRY: PamActionRegistry = {
  [ActionType.STATUS]: {
    title: 'Statut du navire',
    component: MissionActionItemStatus
  }
}

type PamActionRegistryHook = {} & PamActionRegistry & ActionRegistryHook

export function usePamActionRegistry(actionType: ActionType): PamActionRegistryHook {
  const common = useActionRegistry(actionType)
  const pam = PAM_ACTION_REGISTRY[actionType]
  return { ...pam, ...common }
}
