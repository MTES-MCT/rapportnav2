import { Icon, IconProps, THEME } from '@mtes-mct/monitor-ui'
import { FunctionComponent } from 'react'
import { ActionGroupType, ActionType } from '../../common/types/action-type'
import { MissionReportTypeEnum } from '../../common/types/mission-types'
import MissionTimelineItemControlCard from '../../mission-timeline/components/elements/mission-timeline-item-control-card'
import MissionTimelineItemGenericCard from '../../mission-timeline/components/elements/mission-timeline-item-generic-card'
import MissionTimelineItemNoteCard from '../../mission-timeline/components/elements/mission-timeline-item-note-card'
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

const TIME_LINE_DROPDOWN_ULAM_ITEMS_OFFICE: TimelineDropdownItem[] = [
  {
    type: ActionGroupType.ADMINISTRATIVE_GROUP,
    icon: Icon.MissionAction,
    disabled: true,
    dropdownText: 'Préparation / suivi des contrôles',
    subItems: [
      { type: ActionType.INQUIRY, dropdownText: 'Preparation de ctl / rédaction de PV' },
      { type: ActionType.INQUIRY_HEARING, dropdownText: `Preparation et conduite d'audition`, disabled: true }
    ]
  },
  {
    type: ActionType.COMMUNICATION,
    icon: Icon.Contact,
    dropdownText: `Acceuil public / communication`,
    disabled: true
  },
  { type: ActionType.TRAINING, icon: Icon.License, dropdownText: `Ajouter une formation`, disabled: true },
  { type: ActionType.NOTE, icon: Icon.Note, dropdownText: 'Ajouter une note libre' },
  {
    type: ActionType.UNIT_MANAGEMENT,
    icon: Icon.GroupPerson,
    dropdownText: `Vie et gestion de l'unité`,
    disabled: true
  }
]

const TIME_LINE_DROPDOWN_ULAM_ITEMS_FIELD: TimelineDropdownItem[] = [
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
  }
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
    component: MissionTimelineItemNoteCard
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
  },
  [ActionType.INQUIRY]: {
    style: {
      backgroundColor: THEME.color.blueGray25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.MissionAction,
    title: 'Contrôle croisé',
    component: MissionTimelineItemGenericCard
  }
}

interface UlamTimelineRegistrHook {
  getTimeline: (actionType: ActionType) => Timeline
  getTimelineDropdownItems: (missionReportType?: MissionReportTypeEnum) => TimelineDropdownItem[]
}

export function useUlamTimelineRegistry(): UlamTimelineRegistrHook {
  const getTimeline = (actionType: ActionType) => TIMELINE_ULAM_REGISTRY[actionType] ?? ({} as Timeline)

  const getTimelineDropdownItems = (missionReportType?: MissionReportTypeEnum): TimelineDropdownItem[] => {
    if (missionReportType === MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT) return []
    if (missionReportType === MissionReportTypeEnum.OFFICE_REPORT) return TIME_LINE_DROPDOWN_ULAM_ITEMS_OFFICE
    return TIME_LINE_DROPDOWN_ULAM_ITEMS_FIELD.concat(TIME_LINE_DROPDOWN_ULAM_ITEMS_OFFICE)
  }
  return { getTimelineDropdownItems, getTimeline }
}
