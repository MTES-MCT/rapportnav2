import { Icon, IconProps, THEME } from '@mtes-mct/monitor-ui'
import { FunctionComponent } from 'react'
import { ActionGroupType, ActionType } from '../../common/types/action-type'
import MissionTimelineItemControlCard from '../../mission-timeline/components/elements/mission-timeline-item-control-card'
import MissionTimelineItemGenericCard from '../../mission-timeline/components/elements/mission-timeline-item-generic-card'
import MissionTimelineItemRescueCard from '../../mission-timeline/components/elements/mission-timeline-item-rescue-card'
import MissionTimelineItemSurveillanceCard from '../../mission-timeline/components/elements/mission-timeline-item-surveillance-card'
import { Timeline, TimelineDropdownItem, TimelineRegistry } from '../../mission-timeline/hooks/use-timeline'
import { MissionTimelineAction } from '../../mission-timeline/types/mission-timeline-output'

export type ActionStyle = {
  color?: string
  minHeight?: number
  borderColor?: string
  backgroundColor?: string
}

export type ActionTimeline = {
  title?: string
  style: ActionStyle
  noPadding?: boolean
  type: ActionType
  component: FunctionComponent<{
    title?: string
    isSelected?: boolean
    action?: MissionTimelineAction
    icon?: FunctionComponent<IconProps>
    prevAction?: MissionTimelineAction
  }>
  icon?: FunctionComponent<IconProps>
}

const TIME_LINE_DROPDOWN_PAM_ITEMS: TimelineDropdownItem[] = [
  {
    type: ActionGroupType.CONTROL_GROUP,
    icon: Icon.ControlUnit,
    dropdownText: 'Ajouter des contrôles',
    subItems: [{ type: ActionType.CONTROL, dropdownText: 'Contrôle de navire' }]
  },
  {
    type: ActionGroupType.SURVEILLANCE_GROUP,
    icon: Icon.Observation,
    dropdownText: 'Ajouter des surveillances',
    subItems: [{ type: ActionType.NAUTICAL_EVENT, dropdownText: 'Surveillance de manifestation nautique' }]
  },
  { type: ActionType.RESCUE, icon: Icon.Rescue, dropdownText: 'Ajouter une assistance / sauvetage' },
  {
    type: ActionGroupType.OTHER_GROUP,
    icon: Icon.More,
    dropdownText: 'Ajouter une autre activité terrain',
    subItems: [
      { type: ActionType.ANTI_POLLUTION, dropdownText: 'Opé. de lutte anti-pollution' },
      { type: ActionType.ILLEGAL_IMMIGRATION, dropdownText: `Opé. de lutte contre l'immigration illégale` },
      { type: ActionType.REPRESENTATION, dropdownText: 'Représentation' }
    ]
  },
  { type: ActionType.NOTE, icon: Icon.Note, dropdownText: 'Ajouter une note libre' }
]

const TIMELINE_ULAM_REGISTRY: TimelineRegistry = {
  [ActionType.CONTROL]: {
    style: { backgroundColor: THEME.color.white, borderColor: THEME.color.lightGray },
    title: 'Contrôles',
    icon: Icon.ControlUnit,
    component: MissionTimelineItemControlCard
  },
  [ActionType.NOTE]: {
    style: { backgroundColor: THEME.color.blueYonder25, borderColor: THEME.color.lightGray },
    title: 'Note libre',
    icon: Icon.Note,
    component: MissionTimelineItemGenericCard
  },
  [ActionType.NAUTICAL_EVENT]: {
    style: {
      backgroundColor: THEME.color.blueGray25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.More,
    title: 'Surveillance de manifestation nautique',
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
    title: 'Opé. de lutte anti-pollution',
    component: MissionTimelineItemGenericCard
  },
  [ActionType.ILLEGAL_IMMIGRATION]: {
    style: {
      backgroundColor: THEME.color.blueGray25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.More,
    title: `Opé. de lutte contre l'immigration illégale`,
    component: MissionTimelineItemGenericCard
  },
  [ActionType.SURVEILLANCE]: {
    style: { backgroundColor: '#e5e5eb', borderColor: THEME.color.lightGray },
    icon: Icon.Observation,
    title: 'Surveillance Environnement',
    component: MissionTimelineItemSurveillanceCard
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
  [ActionType.BAAEM_PERMANENCE]: {
    style: {
      backgroundColor: THEME.color.blueGray25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.More,
    title: 'Permanence BAAEM',
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

interface UlamTimelineRegistrHook {
  timelineDropdownItems: TimelineDropdownItem[]
  getTimeline: (actionType: ActionType) => Timeline
}

export function useUlamTimelineRegistry(): UlamTimelineRegistrHook {
  const getTimeline = (actionType: ActionType) => TIMELINE_ULAM_REGISTRY[actionType] ?? ({} as Timeline)
  return { timelineDropdownItems: TIME_LINE_DROPDOWN_PAM_ITEMS, getTimeline }
}
