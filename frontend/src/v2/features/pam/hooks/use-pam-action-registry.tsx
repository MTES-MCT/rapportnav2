import { Action } from '@common/types/action-types'
import { ActionTypeEnum } from '@common/types/env-mission-types'
import { ActionRegistryHook, ActionRegistryItem, useActionRegistry } from '../../common/hooks/use-action-registry'
import MissionTimelineItemStatusCardFooter from '../../mission-timeline/components/elements/mission-timeline-item-status-card-footer'
import MissionTimelineItemStatusCardTitle from '../../mission-timeline/components/elements/mission-timeline-item-status-card-title'

type PamActionRegistry = {
  [key in ActionTypeEnum]?: ActionRegistryItem
}
const PAM_ACTION_REGISTRY: PamActionRegistry = {
  [ActionTypeEnum.STATUS]: {
    style: {
      minHeight: 0
    },
    title: 'Statut du navire',
    timeline: {
      noPadding: true,
      dropdownText: 'Ajouter des contrôles',
      getCardTitle: (action?: Action, isSelected?: boolean) => (
        <MissionTimelineItemStatusCardTitle action={action} isSelected={isSelected} />
      ),
      getCardFooter: (action?: Action, prevAction?: Action) => (
        <MissionTimelineItemStatusCardFooter prevAction={prevAction} />
      )
    },
    hasStatusTag: true
  }
}

type PamActionRegistryHook = {} & PamActionRegistry & ActionRegistryHook

export function usePamActionRegistry(actionType: ActionTypeEnum): PamActionRegistryHook {
  const common = useActionRegistry(actionType)
  const ulam = PAM_ACTION_REGISTRY[actionType]
  return { ...ulam, ...common }
}
