import { Icon } from '@mtes-mct/monitor-ui'
import { ActionRegistryHook, ActionRegistryItem, useActionRegistry } from '../../common/hooks/use-action-registry'
import { ActionType } from '../../common/types/action-type'
import MissionActionItemCommunication from '../../mission-action/components/elements/mission-action-item-communication'
import MissionActionItemGenericDateObservation from '../../mission-action/components/elements/mission-action-item-generic-date-observation'
import MissionActionItemHearingConduct from '../../mission-action/components/elements/mission-action-item-hearing-conduct'
import MissionActionItemInquiry from '../../mission-action/components/elements/mission-action-item-inquiry'
import MissionActionItemMeeting from '../../mission-action/components/elements/mission-action-item-meeting'
import MissionActionItemNavSurveillance from '../../mission-action/components/elements/mission-action-item-nav-surveillance'
import MissionActionItemOtherFieldActivity from '../../mission-action/components/elements/mission-action-item-other-field-activity'
import MissionActionItemPublicOrder from '../../mission-action/components/elements/mission-action-item-public-order'
import MissionActionItemPvDrafting from '../../mission-action/components/elements/mission-action-item-pv-drafting'
import MissionActionItemResourceMaintenance from '../../mission-action/components/elements/mission-action-item-resource-maintenance'
import MissionActionItemSecurityVisit from '../../mission-action/components/elements/mission-action-item-security-visit'
import MissionActionItemTraining from '../../mission-action/components/elements/mission-action-item-training'
import MissionActionItemUnitManagementPlanning from '../../mission-action/components/elements/mission-action-item-unit-management-planning'
import MissionActionItemUnitManagementTraining from '../../mission-action/components/elements/mission-action-item-unit-management-training'

type UlamActionRegistry = {
  [key in ActionType]?: ActionRegistryItem
}

const ULAM_ACTION_REGISTRY: UlamActionRegistry = {
  [ActionType.NAUTICAL_EVENT]: {
    icon: Icon.Observation,
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
  [ActionType.MARITIME_SURVEILLANCE]: {
    icon: Icon.Observation,
    title: `Surveillance générale maritime`,
    component: MissionActionItemNavSurveillance
  },
  [ActionType.OTHER]: {
    icon: Icon.More,
    title: `Autre activité terrain - autre`,
    component: MissionActionItemOtherFieldActivity
  },
  [ActionType.UNIT_MANAGEMENT_OTHER]: {
    icon: Icon.GroupPerson,
    title: `Gestion de l'unité - autre`,
    component: MissionActionItemGenericDateObservation
  },
  [ActionType.SECURITY_VISIT]: {
    icon: Icon.More,
    title: 'Visite sécurité',
    component: MissionActionItemSecurityVisit
  },
  [ActionType.PUBLIC_ORDER]: {
    icon: Icon.More,
    title: 'Opé. sûreté maritime',
    component: MissionActionItemPublicOrder
  }
}

type UlamActionRegistryHook = {} & UlamActionRegistry & ActionRegistryHook

export function useUlamActionRegistry(actionType: ActionType): UlamActionRegistryHook {
  const common = useActionRegistry(actionType)
  const ulam = ULAM_ACTION_REGISTRY[actionType]
  return { ...ulam, ...common }
}
