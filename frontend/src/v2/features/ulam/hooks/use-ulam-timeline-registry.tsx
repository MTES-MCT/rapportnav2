import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { ActionGroupType, ActionType } from '../../common/types/action-type'
import { MissionReportTypeEnum } from '../../common/types/mission-types'
import MissionTimelineItemControlCard from '../../mission-timeline/components/elements/mission-timeline-item-control-card'
import MissionTimelineItemGenericCard from '../../mission-timeline/components/elements/mission-timeline-item-generic-card'
import MissionTimelineItemNoteCard from '../../mission-timeline/components/elements/mission-timeline-item-note-card'
import MissionTimelineItemRescueCard from '../../mission-timeline/components/elements/mission-timeline-item-rescue-card'
import MissionTimelineItemSurveillanceCard from '../../mission-timeline/components/elements/mission-timeline-item-surveillance-card'
import { Timeline, TimelineDropdownItem, TimelineRegistry } from '../../mission-timeline/hooks/use-timeline'

const TIME_LINE_DROPDOWN_ULAM_ITEMS_OFFICE: TimelineDropdownItem[] = [
  {
    type: ActionGroupType.ADMINISTRATIVE_GROUP,
    icon: Icon.MissionAction,
    dropdownText: 'Préparation / suivi des contrôles',
    subItems: [
      { type: ActionType.PV_DRAFTING, dropdownText: 'Preparation de ctl / rédaction de PV' },
      { type: ActionType.HEARING_CONDUCT, dropdownText: `Preparation et conduite d'audition` }
    ]
  },
  {
    type: ActionType.COMMUNICATION,
    icon: Icon.Contact,
    dropdownText: `Acceuil public / communication`
  },
  { type: ActionType.TRAINING, icon: Icon.License, dropdownText: `Ajouter une formation` },
  { type: ActionType.NOTE, icon: Icon.Note, dropdownText: 'Ajouter une note libre' },
  {
    type: ActionGroupType.UNIT_MANAGEMENT_GROUP,
    icon: Icon.GroupPerson,
    dropdownText: `Vie et gestion de l'unité`,
    subItems: [
      { type: ActionType.UNIT_MANAGEMENT_PLANNING, dropdownText: `Getion de l'unité (planning...)` },
      { type: ActionType.RESOURCES_MAINTENANCE, dropdownText: `Entretien des moyens` },
      { type: ActionType.MEETING, dropdownText: `Réunions` },
      { type: ActionType.UNIT_MANAGEMENT_TRAINING, dropdownText: `Entraînement (tir, plongée...)` },
      { type: ActionType.UNIT_MANAGEMENT_OTHER, dropdownText: `Autre` }
    ]
  }
]

const TIME_LINE_DROPDOWN_ULAM_ITEMS_FIELD: TimelineDropdownItem[] = [
  {
    type: ActionGroupType.CONTROL_GROUP,
    icon: Icon.ControlUnit,
    dropdownText: 'Ajouter des contrôles',
    subItems: [
      { type: ActionType.CONTROL, dropdownText: 'Contrôle de navire' },
      { type: ActionType.CONTROL_SECTOR, dropdownText: 'Contrôle de filière' },
      { type: ActionType.CONTROL_NAUTICAL_LEISURE, dropdownText: 'Contrôle de loisirs nautiques' },
      {
        type: ActionType.CONTROL_SLEEPING_FISHING_GEAR,
        dropdownText: `Contrôle d'engin de pêche dormant`
      },
      { type: ActionType.OTHER_CONTROL, dropdownText: 'Autre contrôle' }
      //{ type: ActionType.INQUIRY, dropdownText: 'Contrôle croisés', disabled: true } //TODO: remove com as soon as inquiry link on mission is made
    ]
  },
  {
    type: ActionGroupType.SURVEILLANCE_GROUP,
    icon: Icon.Observation,
    dropdownText: 'Ajouter des surveillances',
    subItems: [
      { type: ActionType.NAUTICAL_EVENT, dropdownText: 'Surveillance de manifestation nautique' },
      { type: ActionType.LAND_SURVEILLANCE, dropdownText: 'Surveillance générale terrestre' },
      { type: ActionType.MARITIME_SURVEILLANCE, dropdownText: 'Surveillance générale maritime' },
      { type: ActionType.FISHING_SURVEILLANCE, dropdownText: 'Surveillance générale pêche', disabled: true }
    ]
  },
  { type: ActionType.RESCUE, icon: Icon.Rescue, dropdownText: 'Ajouter une assistance / sauvetage' },
  {
    type: ActionGroupType.OTHER_GROUP,
    icon: Icon.More,
    dropdownText: 'Ajouter une autre activité terrain',
    subItems: [
      { type: ActionType.PUBLIC_ORDER, dropdownText: 'Opé. sûreté maritime' },
      { type: ActionType.ANTI_POLLUTION, dropdownText: 'Opé. de lutte anti-pollution' },
      { type: ActionType.ILLEGAL_IMMIGRATION, dropdownText: `Opé. de lutte contre l'immigration illégale` },
      { type: ActionType.REPRESENTATION, dropdownText: 'Représentation' },
      { type: ActionType.SECURITY_VISIT, dropdownText: 'Visite sécurité' },
      { type: ActionType.OTHER, dropdownText: 'Autre' }
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
      backgroundColor: THEME.color.gainsboro,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.Observation,
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
    title: `Opé. sûreté maritime`,
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
    style: {
      backgroundColor: THEME.color.blueGray25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.More,
    title: 'Autre activité terrain - autre',
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
  },
  [ActionType.UNIT_MANAGEMENT_PLANNING]: {
    style: {
      backgroundColor: THEME.color.blueYonder25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.GroupPerson,
    title: `Gestion de l'unité (planning)`,
    component: MissionTimelineItemGenericCard
  },
  [ActionType.UNIT_MANAGEMENT_TRAINING]: {
    style: {
      backgroundColor: THEME.color.blueYonder25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.GroupPerson,
    title: `Entraînement`,
    component: MissionTimelineItemGenericCard
  },
  [ActionType.UNIT_MANAGEMENT_OTHER]: {
    style: {
      backgroundColor: THEME.color.blueYonder25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.GroupPerson,
    title: `Gestion de l'unité - autre`,
    component: MissionTimelineItemGenericCard
  },
  [ActionType.TRAINING]: {
    style: {
      backgroundColor: THEME.color.blueYonder25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.License,
    title: `Formation`,
    component: MissionTimelineItemGenericCard
  },
  [ActionType.RESOURCES_MAINTENANCE]: {
    style: {
      backgroundColor: THEME.color.blueYonder25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.GroupPerson,
    title: `Entretien des moyens`,
    component: MissionTimelineItemGenericCard
  },
  [ActionType.MEETING]: {
    style: {
      backgroundColor: THEME.color.blueYonder25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.GroupPerson,
    title: `Réunions`,
    component: MissionTimelineItemGenericCard
  },
  [ActionType.PV_DRAFTING]: {
    style: {
      backgroundColor: THEME.color.blueYonder25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.MissionAction,
    title: `Preparation de ctl / rédaction de PV`,
    component: MissionTimelineItemGenericCard
  },
  [ActionType.HEARING_CONDUCT]: {
    style: {
      backgroundColor: THEME.color.blueYonder25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.MissionAction,
    title: `Preparation et conduite d'audition`,
    component: MissionTimelineItemGenericCard
  },
  [ActionType.LAND_SURVEILLANCE]: {
    style: {
      backgroundColor: THEME.color.gainsboro,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.Observation,
    title: `Surveillance générale terrestre`,
    component: MissionTimelineItemGenericCard
  },
  [ActionType.MARITIME_SURVEILLANCE]: {
    style: {
      backgroundColor: THEME.color.gainsboro,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.Observation,
    title: `Surveillance générale maritime`,
    component: MissionTimelineItemGenericCard
  },
  [ActionType.COMMUNICATION]: {
    style: {
      backgroundColor: THEME.color.blueYonder25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.Contact,
    title: `Acceuil public / communication`,
    component: MissionTimelineItemGenericCard
  },
  [ActionType.OTHER_CONTROL]: {
    style: { backgroundColor: THEME.color.white, borderColor: THEME.color.lightGray },
    icon: Icon.ControlUnit,
    title: `Contrôle - autre`,
    component: MissionTimelineItemControlCard
  },
  [ActionType.CONTROL_NAUTICAL_LEISURE]: {
    style: { backgroundColor: THEME.color.white, borderColor: THEME.color.lightGray },
    icon: Icon.ControlUnit,
    title: `Contrôle - Loisirs nautiques`,
    component: MissionTimelineItemControlCard
  },
  [ActionType.CONTROL_SLEEPING_FISHING_GEAR]: {
    style: { backgroundColor: THEME.color.white, borderColor: THEME.color.lightGray },
    icon: Icon.ControlUnit,
    title: `Contrôle - engin de pêche dormant`,
    component: MissionTimelineItemControlCard
  },
  [ActionType.CONTROL_SECTOR]: {
    style: { backgroundColor: THEME.color.white, borderColor: THEME.color.lightGray },
    icon: Icon.ControlUnit,
    title: `Contrôle - filière`,
    component: MissionTimelineItemControlCard
  },
  [ActionType.SECURITY_VISIT]: {
    style: {
      backgroundColor: THEME.color.blueGray25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.More,
    title: 'Visite sécurité',
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
