import { Icon } from '@mtes-mct/monitor-ui'
import { ActionRegistryHook, ActionRegistryItem, useActionRegistry } from '../../common/hooks/use-action-registry'
import { ActionType } from '../../common/types/action-type'
import MissionActionItemCommunication from '../../mission-action/components/elements/mission-action-item-communication'
import MissionActionItemHearingConduct from '../../mission-action/components/elements/mission-action-item-hearing-conduct'
import MissionActionItemInquiry from '../../mission-action/components/elements/mission-action-item-inquiry'
import MissionActionItemMeeting from '../../mission-action/components/elements/mission-action-item-meeting'
import MissionActionItemNavSurveillance from '../../mission-action/components/elements/mission-action-item-nav-surveillance'
import MissionActionItemPvDrafting from '../../mission-action/components/elements/mission-action-item-pv-drafting'
import MissionActionItemResourceMaintenance from '../../mission-action/components/elements/mission-action-item-resource-maintenance'
import MissionActionItemTraining from '../../mission-action/components/elements/mission-action-item-training'
import MissionActionItemUnitManagementPlanning from '../../mission-action/components/elements/mission-action-item-unit-management-planning'
import MissionActionItemUnitManagementTraining from '../../mission-action/components/elements/mission-action-item-unit-management-training'

type UlamActionRegistry = {
  [key in ActionType]?: ActionRegistryItem
}

const ULAM_ACTION_REGISTRY: UlamActionRegistry = {
  [ActionType.NAUTICAL_EVENT]: {
    icon: Icon.More,
    title: 'Surveillance de manifestation nautique',
    component: MissionActionItemNavSurveillance
  },
  [ActionType.INQUIRY]: {
    icon: Icon.MissionAction,
    title: 'Contrôle croisé',
    component: MissionActionItemInquiry
  },
  [ActionType.UNIT_MANAGEMENT_PLANNING]: {
    icon: Icon.GroupPerson,
    title: `Gestion de l'unité`,
    component: MissionActionItemUnitManagementPlanning
  },
  [ActionType.UNIT_MANAGEMENT_TRAINING]: {
    icon: Icon.GroupPerson,
    title: `Entraînement`,
    component: MissionActionItemUnitManagementTraining
  },
  [ActionType.TRAINING]: {
    icon: Icon.License,
    title: `Formation`,
    component: MissionActionItemTraining
  },
  [ActionType.HEARING_CONDUCT]: {
    icon: Icon.MissionAction,
    title: `Preparation et conduite d'audition`,
    component: MissionActionItemHearingConduct
  },
  [ActionType.COMMUNICATION]: {
    icon: Icon.Contact,
    title: `Acceuil public / communication`,
    component: MissionActionItemCommunication
  },
  [ActionType.RESOURCES_MAINTENANCE]: {
    icon: Icon.GroupPerson,
    title: `Entretien des moyens`,
    component: MissionActionItemResourceMaintenance
  },
  [ActionType.MEETING]: {
    icon: Icon.GroupPerson,
    title: `Réunions`,
    component: MissionActionItemMeeting
  },
  [ActionType.PV_DRAFTING]: {
    icon: Icon.MissionAction,
    title: `Preparation de ctl / rédaction de PV`,
    component: MissionActionItemPvDrafting
  },
  [ActionType.LAND_SURVEILLANCE]: {
    icon: Icon.Observation,
    title: `Surveillance générale terrestre`,
    component: MissionActionItemNavSurveillance
  },
  [ActionType.FISHING_SURVEILLANCE]: {
    icon: Icon.Observation,
    title: `Surveillance générale pêche`,
    component: MissionActionItemNavSurveillance
  }
}

type UlamActionRegistryHook = {} & UlamActionRegistry & ActionRegistryHook

export function useUlamActionRegistry(actionType: ActionType): UlamActionRegistryHook {
  const common = useActionRegistry(actionType)
  const ulam = ULAM_ACTION_REGISTRY[actionType]
  return { ...ulam, ...common }
}
