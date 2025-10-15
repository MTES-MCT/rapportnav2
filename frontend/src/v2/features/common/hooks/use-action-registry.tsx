import { Icon, IconProps } from '@mtes-mct/monitor-ui'
import { FunctionComponent } from 'react'
import MissionActionItemAntiPollution from '../../mission-action/components/elements/mission-action-item-anti-pollution'
import MissionActionItemBAAEMPermanence from '../../mission-action/components/elements/mission-action-item-baaem-performance'
import MissionActionItemContact from '../../mission-action/components/elements/mission-action-item-contact'
import MissionActionItemControl from '../../mission-action/components/elements/mission-action-item-control'
import MissionActionItemNauticalLeisureControl from '../../mission-action/components/elements/mission-action-item-control-nautical-leisure'
import MissionActionItemOtherControl from '../../mission-action/components/elements/mission-action-item-control-other'
import MissionActionItemSectorControl from '../../mission-action/components/elements/mission-action-item-control-sector'
import MissionActionItemSleepingFishingGearControl from '../../mission-action/components/elements/mission-action-item-control-sleeping-fishing-gear'
import MissionActionItemIllegalImmigration from '../../mission-action/components/elements/mission-action-item-illegal-immigration'
import MissionActionItemNote from '../../mission-action/components/elements/mission-action-item-note'
import MissionActionItemPublicOrder from '../../mission-action/components/elements/mission-action-item-public-order'
import MissionActionItemRepresentation from '../../mission-action/components/elements/mission-action-item-representation'
import MissionActionItemRescue from '../../mission-action/components/elements/mission-action-item-rescue'
import MissionActionItemSecurityVisit from '../../mission-action/components/elements/mission-action-item-security-visit'
import MissionActionItemSurveillance from '../../mission-action/components/elements/mission-action-item-surveillance'
import MissionActionItemVigimer from '../../mission-action/components/elements/mission-action-item-vigimer'
import { ActionType } from '../types/action-type'
import { MissionAction } from '../types/mission-action'

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
  [ActionType.PUBLIC_ORDER]: {
    icon: Icon.More,
    title: `Maintien de l'ordre public`,
    component: MissionActionItemPublicOrder
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
  [ActionType.OTHER_CONTROL]: {
    title: 'Contrôle - Autre',
    icon: Icon.ControlUnit,
    component: MissionActionItemOtherControl
  },
  [ActionType.CONTROL_NAUTICAL_LEISURE]: {
    title: `Contrôle de loisirs nautiques`,
    icon: Icon.ControlUnit,
    component: MissionActionItemNauticalLeisureControl
  },
  [ActionType.CONTROL_SLEEPING_FISHING_GEAR]: {
    title: `Contrôle d'engins de pêche dormant`,
    icon: Icon.ControlUnit,
    component: MissionActionItemSleepingFishingGearControl
  },
  [ActionType.CONTROL_SECTOR]: {
    title: `Contrôle d'établissement filière`,
    icon: Icon.ControlUnit,
    component: MissionActionItemSectorControl
  },
  [ActionType.SECURITY_VISIT]: {
    icon: Icon.More,
    title: 'Visite sécurité',
    component: MissionActionItemSecurityVisit
  }
}

export type ActionRegistryHook = ActionRegistryItem

export function useActionRegistry(actionType: ActionType): ActionRegistryHook {
  return { ...(ACTION_REGISTRY[actionType] ?? ({} as ActionRegistryItem)) }
}
