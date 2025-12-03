import { Icon, IconProps } from '@mtes-mct/monitor-ui'
import { FunctionComponent } from 'react'
import MissionActionItemAntiPollution from '../../mission-action/components/elements/mission-action-item-anti-pollution'
import MissionActionItemBAAEMPermanence from '../../mission-action/components/elements/mission-action-item-baaem-performance'
import MissionActionItemContact from '../../mission-action/components/elements/mission-action-item-contact'
import MissionActionItemControl from '../../mission-action/components/elements/mission-action-item-control'
import MissionActionItemIllegalImmigration from '../../mission-action/components/elements/mission-action-item-illegal-immigration'
import MissionActionItemNote from '../../mission-action/components/elements/mission-action-item-note'
import MissionActionItemRepresentation from '../../mission-action/components/elements/mission-action-item-representation'
import MissionActionItemRescue from '../../mission-action/components/elements/mission-action-item-rescue'
import MissionActionItemSurveillance from '../../mission-action/components/elements/mission-action-item-surveillance'
import MissionActionItemVigimer from '../../mission-action/components/elements/mission-action-item-vigimer'
import { ActionType } from '../types/action-type'
import { MissionAction } from '../types/mission-action'
import MissionActionItemUnitManagementTraining from '../../mission-action/components/elements/mission-action-item-unit-management-training.tsx'

export type ActionRegistryItem = {
  title?: string
  icon?: FunctionComponent<IconProps>
  component?: FunctionComponent<{
    action: MissionAction
    onChange: (newAction: MissionAction, debounceTime?: number) => Promise<unknown>
    isMissionFinished?: boolean
  }>
}

export type ActionRegistry = {
  [key in ActionType]?: ActionRegistryItem
}

const ACTION_REGISTRY: ActionRegistry = {
  [ActionType.CONTROL]: {
    title: 'Contrôles',
    icon: Icon.ControlUnit,
    component: MissionActionItemControl
  },
  [ActionType.SURVEILLANCE]: {
    icon: Icon.Observation,
    title: 'Surveillance Environnement',
    component: MissionActionItemSurveillance
  },
  [ActionType.NOTE]: {
    title: 'Note libre',
    icon: Icon.Note,
    component: MissionActionItemNote
  },
  [ActionType.VIGIMER]: {
    icon: Icon.More,
    title: 'Permanence Vigimer',
    component: MissionActionItemVigimer
  },
  [ActionType.RESCUE]: {
    title: 'Assistance et sauvetage',
    icon: Icon.Rescue,
    component: MissionActionItemRescue
  },
  [ActionType.REPRESENTATION]: {
    icon: Icon.More,
    title: 'Représentation',
    component: MissionActionItemRepresentation
  },
  [ActionType.ANTI_POLLUTION]: {
    icon: Icon.More,
    title: 'Opération de lutte anti-pollution',
    component: MissionActionItemAntiPollution
  },
  [ActionType.BAAEM_PERMANENCE]: {
    icon: Icon.More,
    title: 'Permanence BAAEM',
    component: MissionActionItemBAAEMPermanence
  },
  [ActionType.ILLEGAL_IMMIGRATION]: {
    icon: Icon.More,
    title: `Lutte contre l'immigration illégale`,
    component: MissionActionItemIllegalImmigration
  },
  [ActionType.OTHER]: {
    icon: Icon.More
  },
  [ActionType.CONTACT]: {
    icon: Icon.Observation,
    title: 'Contact',
    component: MissionActionItemContact
  },
  [ActionType.UNIT_MANAGEMENT_TRAINING]: {
    icon: Icon.GroupPerson,
    title: `Entraînement`,
    component: MissionActionItemUnitManagementTraining
  }
}

export type ActionRegistryHook = ActionRegistryItem

export function useActionRegistry(actionType: ActionType): ActionRegistryHook {
  return { ...(ACTION_REGISTRY[actionType] ?? ({} as ActionRegistryItem)) }
}
