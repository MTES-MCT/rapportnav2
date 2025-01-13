import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { ActionGroupType, ActionType } from '../../common/types/action-type'
import MissionTimelineItemControlCard from '../../mission-timeline/components/elements/mission-timeline-item-control-card'
import MissionTimelineItemGenericCard from '../../mission-timeline/components/elements/mission-timeline-item-generic-card'
import MissionTimelineItemRescueCard from '../../mission-timeline/components/elements/mission-timeline-item-rescue-card'
import MissionTimelineItemStatusCard from '../../mission-timeline/components/elements/mission-timeline-item-status-card'
import MissionTimelineItemSurveillanceCard from '../../mission-timeline/components/elements/mission-timeline-item-surveillance-card'
import { Timeline, TimelineDropdownItem, TimelineRegistry } from '../../mission-timeline/hooks/use-timeline'

const TIME_LINE_DROPDOWN_PAM_ITEMS: TimelineDropdownItem[] = [
  { type: ActionType.CONTROL, icon: Icon.ControlUnit, dropdownText: 'Ajouter des contrôles' },
  { type: ActionType.NOTE, icon: Icon.Note, dropdownText: 'Ajouter une note libre' },
  { type: ActionType.RESCUE, icon: Icon.Rescue, dropdownText: 'Ajouter une assistance / sauvetage' },
  {
    icon: Icon.More,
    type: ActionGroupType.OTHER_GROUP,
    dropdownText: 'Ajouter une autre activité de mission',
    subItems: [
      { type: ActionType.NAUTICAL_EVENT, dropdownText: 'Sécu de manifestation nautique' },
      { type: ActionType.BAAEM_PERMANENCE, dropdownText: 'Permanence BAAEM' },
      { type: ActionType.VIGIMER, dropdownText: 'Permanence Vigimer' },
      { type: ActionType.ANTI_POLLUTION, dropdownText: 'Opération de lutte anti-pollution' },
      { type: ActionType.ILLEGAL_IMMIGRATION, dropdownText: `Lutte contre l'immigration illégale` },
      { type: ActionType.PUBLIC_ORDER, dropdownText: `Maintien de l'ordre public` },
      { type: ActionType.REPRESENTATION, dropdownText: 'Représentation' }
    ]
  }
]

const TIMELINE_PAM_REGISTRY: TimelineRegistry = {
  [ActionType.STATUS]: {
    noPadding: true,
    style: { minHeight: 0 },
    title: 'Statut du navire',
    component: MissionTimelineItemStatusCard
  },
  [ActionType.CONTROL]: {
    style: { backgroundColor: THEME.color.white, borderColor: THEME.color.lightGray },
    title: 'Contrôles',
    icon: Icon.ControlUnit,
    component: MissionTimelineItemControlCard
  },
  [ActionType.SURVEILLANCE]: {
    style: { backgroundColor: '#e5e5eb', borderColor: THEME.color.lightGray },
    icon: Icon.Observation,
    title: 'Surveillance Environnement',
    component: MissionTimelineItemSurveillanceCard
  },
  [ActionType.NOTE]: {
    style: { backgroundColor: THEME.color.blueYonder25, borderColor: THEME.color.lightGray },
    title: 'Note libre',
    icon: Icon.Note,
    component: MissionTimelineItemGenericCard
  },
  [ActionType.VIGIMER]: {
    style: {
      backgroundColor: THEME.color.blueGray25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.More,
    title: 'Permanence Vigimer',
    component: MissionTimelineItemGenericCard
  },
  [ActionType.NAUTICAL_EVENT]: {
    style: {
      backgroundColor: THEME.color.blueGray25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.More,
    title: 'Manifestation nautique',
    component: MissionTimelineItemGenericCard
  },
  [ActionType.RESCUE]: {
    style: {
      backgroundColor: THEME.color.goldenPoppy25,
      borderColor: THEME.color.blueYonder25
    },
    title: 'Assistance et sauvetage',
    icon: Icon.Rescue,
    component: MissionTimelineItemRescueCard
  },
  [ActionType.REPRESENTATION]: {
    style: {
      backgroundColor: THEME.color.blueGray25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.More,
    title: 'Représentation',
    component: MissionTimelineItemGenericCard
  },
  [ActionType.PUBLIC_ORDER]: {
    style: {
      backgroundColor: THEME.color.blueGray25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.More,
    title: `Maintien de l'ordre public`,
    component: MissionTimelineItemGenericCard
  },
  [ActionType.ANTI_POLLUTION]: {
    style: {
      backgroundColor: THEME.color.blueGray25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.More,
    title: 'Opération de lutte anti-pollution',
    component: MissionTimelineItemGenericCard
  },
  [ActionType.BAAEM_PERMANENCE]: {
    style: {
      backgroundColor: THEME.color.blueGray25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.More,
    title: 'Permanence BAAEM',
    component: MissionTimelineItemGenericCard
  },
  [ActionType.ILLEGAL_IMMIGRATION]: {
    style: {
      backgroundColor: THEME.color.blueGray25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.More,
    title: `Lutte contre l'immigration illégale`,
    component: MissionTimelineItemGenericCard
  },
  [ActionType.OTHER]: {
    style: {},
    icon: Icon.More,
    component: MissionTimelineItemGenericCard
  },
  [ActionType.CONTACT]: {
    style: { backgroundColor: THEME.color.blueYonder25, borderColor: THEME.color.lightGray },
    icon: Icon.Observation,
    title: 'Contact',
    component: MissionTimelineItemGenericCard
  }
}

interface PamTimelineRegistrHook {
  timelineDropdownItems: TimelineDropdownItem[]
  getTimeline: (actionType: ActionType) => Timeline
}

export function usePamTimelineRegistry(): PamTimelineRegistrHook {
  const getTimeline = (actionType: ActionType) => TIMELINE_PAM_REGISTRY[actionType] ?? ({} as Timeline)
  return { timelineDropdownItems: TIME_LINE_DROPDOWN_PAM_ITEMS, getTimeline }
}
