import { ActionTypeEnum } from '@common/types/env-mission-types'
import { ActionRegistryHook, ActionRegistryItem, useActionRegistry } from '../../common/hooks/use-action-registry'
import MissionActionItemStatus from '../../mission-action/components/elements/mission-action-item-status'
import MissionTimelineItemStatusCard from '../../mission-timeline/components/elements/mission-timeline-item-status-card'

type PamActionRegistry = {
  [key in ActionTypeEnum]?: ActionRegistryItem
}
const PAM_ACTION_REGISTRY: PamActionRegistry = {
  [ActionTypeEnum.STATUS]: {
    hasStatusTag: true,
    style: {
      minHeight: 0
    },
    title: 'Statut du navire',
    timeline: {
      noPadding: true,
      dropdownText: 'Ajouter des contrôles',
      component: MissionTimelineItemStatusCard
    },
    actionComponent: MissionActionItemStatus
  }
}

type PamActionRegistryHook = {} & PamActionRegistry & ActionRegistryHook

export function usePamActionRegistry(actionType: ActionTypeEnum): PamActionRegistryHook {
  const common = useActionRegistry(actionType)
  const pam = PAM_ACTION_REGISTRY[actionType]
  return { ...pam, ...common }
}
