import { Icon } from '@mtes-mct/monitor-ui'
import { ActionRegistryHook, ActionRegistryItem, useActionRegistry } from '../../common/hooks/use-action-registry'
import { ActionType } from '../../common/types/action-type'
import MissionActionItemNauticalEvent from '../../mission-action/components/elements/mission-action-item-nautical-event'
import MissionActionItemPublicOrder from '../../mission-action/components/elements/mission-action-item-public-order'
import MissionActionItemStatus from '../../mission-action/components/elements/mission-action-item-status'

type PamActionRegistry = {
  [key in ActionType]?: ActionRegistryItem
}

const PAM_ACTION_REGISTRY: PamActionRegistry = {
  [ActionType.STATUS]: {
    title: 'Statut du navire',
    component: MissionActionItemStatus
  },
  [ActionType.NAUTICAL_EVENT]: {
    icon: Icon.More,
    title: 'Manifestation nautique',
    component: MissionActionItemNauticalEvent
  },
  [ActionType.PUBLIC_ORDER]: {
    icon: Icon.More,
    title: `Maintien de l'ordre public`,
    component: MissionActionItemPublicOrder
  }
}

type PamActionRegistryHook = {} & PamActionRegistry & ActionRegistryHook

export function usePamActionRegistry(actionType: ActionType): PamActionRegistryHook {
  const common = useActionRegistry(actionType)
  const pam = PAM_ACTION_REGISTRY[actionType]
  return { ...pam, ...common }
}
