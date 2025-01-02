import { ActionTypeEnum } from '@common/types/env-mission-types'
import { CompletenessForStatsStatusEnum } from '@common/types/mission-types'
import { Icon, IconProps, THEME } from '@mtes-mct/monitor-ui'
import { FunctionComponent } from 'react'
import MissionActionItemAntiPollution from '../../mission-action/components/elements/mission-action-item-anti-pollution'
import MissionActionItemBAAEMPermanence from '../../mission-action/components/elements/mission-action-item-baaem-performance'
import MissionActionItemControl from '../../mission-action/components/elements/mission-action-item-control'
import MissionActionItemIllegalImmigration from '../../mission-action/components/elements/mission-action-item-illegal-immigration'
import MissionActionItemNauticalEvent from '../../mission-action/components/elements/mission-action-item-nautical-event'
import MissionActionItemNote from '../../mission-action/components/elements/mission-action-item-note'
import MissionActionItemPublicOrder from '../../mission-action/components/elements/mission-action-item-public-order'
import MissionActionItemRepresentation from '../../mission-action/components/elements/mission-action-item-representation'
import MissionActionItemRescue from '../../mission-action/components/elements/mission-action-item-rescue'
import MissionActionItemSurveillance from '../../mission-action/components/elements/mission-action-item-surveillance'
import MissionActionItemVigimer from '../../mission-action/components/elements/mission-action-item-vigimer'
import MissionTimelineItemControlCard from '../../mission-timeline/components/elements/mission-timeline-item-control-card'
import MissionTimelineItemGenericCard from '../../mission-timeline/components/elements/mission-timeline-item-generic-card'
import MissionTimelineItemRescueCard from '../../mission-timeline/components/elements/mission-timeline-item-rescue-card'
import MissionTimelineItemSurveillanceCard from '../../mission-timeline/components/elements/mission-timeline-item-surveillance-card'
import { MissionTimelineAction } from '../../mission-timeline/types/mission-timeline-output'
import { MissionAction } from '../types/mission-action'

export type ActionTimeline = {
  dropdownText?: string
  noPadding?: boolean
  component: FunctionComponent<{
    title?: string
    isSelected?: boolean
    action?: MissionTimelineAction
    icon?: FunctionComponent<IconProps>
    prevAction?: MissionTimelineAction
  }>
}

export type ActionStyle = {
  color?: string
  minHeight?: number
  borderColor?: string
  backgroundColor?: string
}

export type ActionRegistryItem = {
  title?: string
  style?: ActionStyle
  timeline: ActionTimeline
  hasStatusTag?: boolean
  icon?: FunctionComponent<IconProps>
  actionComponent?: FunctionComponent<{
    action: MissionAction
    onChange: (newAction: MissionAction, debounceTime?: number) => Promise<unknown>
    isMissionFinished?: boolean
  }>
}

export type ActionRegistry = {
  [key in ActionTypeEnum]?: ActionRegistryItem
}

const ACTION_REGISTRY: ActionRegistry = {
  [ActionTypeEnum.CONTROL]: {
    style: { backgroundColor: THEME.color.white, borderColor: THEME.color.lightGray },
    title: 'Contrôles',
    icon: Icon.ControlUnit,
    timeline: {
      dropdownText: 'Ajouter des contrôles',
      component: MissionTimelineItemControlCard
    },
    actionComponent: MissionActionItemControl
  },
  [ActionTypeEnum.SURVEILLANCE]: {
    style: { backgroundColor: '#e5e5eb', borderColor: THEME.color.lightGray },
    icon: Icon.Observation,
    title: 'Surveillance Environnement',
    timeline: {
      dropdownText: `Surveillance`,
      component: MissionTimelineItemSurveillanceCard
    },
    actionComponent: MissionActionItemSurveillance
  },
  [ActionTypeEnum.NOTE]: {
    style: { backgroundColor: THEME.color.blueYonder25, borderColor: THEME.color.lightGray },
    title: 'Note libre',
    icon: Icon.Note,
    timeline: {
      dropdownText: 'Ajouter une note libre',
      component: MissionTimelineItemGenericCard
    },
    actionComponent: MissionActionItemNote
  },
  [ActionTypeEnum.VIGIMER]: {
    style: {
      backgroundColor: THEME.color.blueGray25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.More,
    title: 'Permanence Vigimer',
    timeline: {
      dropdownText: 'Permanence Vigimer',
      component: MissionTimelineItemGenericCard
    },
    actionComponent: MissionActionItemVigimer
  },
  [ActionTypeEnum.NAUTICAL_EVENT]: {
    style: {
      backgroundColor: THEME.color.blueGray25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.More,
    title: 'Manifestation nautique',
    timeline: {
      dropdownText: 'Sécu de manifestation nautique',
      component: MissionTimelineItemGenericCard
    },
    actionComponent: MissionActionItemNauticalEvent
  },
  [ActionTypeEnum.RESCUE]: {
    style: {
      backgroundColor: THEME.color.goldenPoppy25,
      borderColor: THEME.color.blueYonder25
    },
    title: 'Assistance et sauvetage',
    icon: Icon.Rescue,
    timeline: {
      dropdownText: 'Ajouter une assistance / sauvetage',
      component: MissionTimelineItemRescueCard
    },
    actionComponent: MissionActionItemRescue
  },
  [ActionTypeEnum.REPRESENTATION]: {
    style: {
      backgroundColor: THEME.color.blueGray25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.More,
    title: 'Représentation',
    timeline: {
      dropdownText: 'Représentation',
      component: MissionTimelineItemGenericCard
    },
    actionComponent: MissionActionItemRepresentation
  },
  [ActionTypeEnum.PUBLIC_ORDER]: {
    style: {
      backgroundColor: THEME.color.blueGray25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.More,
    title: `Maintien de l'ordre public`,
    timeline: {
      dropdownText: `Maintien de l'ordre public`,
      component: MissionTimelineItemGenericCard
    },
    actionComponent: MissionActionItemPublicOrder
  },
  [ActionTypeEnum.ANTI_POLLUTION]: {
    style: {
      backgroundColor: THEME.color.blueGray25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.More,
    title: 'Opération de lutte anti-pollution',
    timeline: {
      dropdownText: 'Opération de lutte anti-pollution',
      component: MissionTimelineItemGenericCard
    },
    actionComponent: MissionActionItemAntiPollution
  },
  [ActionTypeEnum.BAAEM_PERMANENCE]: {
    style: {
      backgroundColor: THEME.color.blueGray25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.More,
    title: 'Permanence BAAEM',
    timeline: {
      dropdownText: 'Permanence BAAEM',
      component: MissionTimelineItemGenericCard
    },
    actionComponent: MissionActionItemBAAEMPermanence
  },
  [ActionTypeEnum.ILLEGAL_IMMIGRATION]: {
    style: {
      backgroundColor: THEME.color.blueGray25,
      borderColor: THEME.color.lightGray
    },
    icon: Icon.More,
    title: `Lutte contre l'immigration illégale`,
    timeline: {
      dropdownText: `Lutte contre l'immigration illégale`,
      component: MissionTimelineItemGenericCard
    },
    actionComponent: MissionActionItemIllegalImmigration
  },
  [ActionTypeEnum.OTHER]: {
    style: {},
    icon: Icon.More,
    timeline: {
      dropdownText: 'Ajouter une autre activité de mission',
      component: MissionTimelineItemGenericCard
    }
  },
  [ActionTypeEnum.CONTACT]: {
    style: {},
    timeline: {
      dropdownText: `Contact`,
      component: MissionTimelineItemGenericCard
    }
  }
}

export type ActionRegistryHook = {
  isIncomplete: (action?: MissionTimelineAction) => boolean
} & ActionRegistryItem

export function useActionRegistry(actionType: ActionTypeEnum): ActionRegistryHook {
  const getAction = () => ACTION_REGISTRY[actionType] ?? ({} as ActionRegistryItem)
  const action = getAction()
  const isIncomplete = (action?: MissionTimelineAction) =>
    action?.completenessForStats?.status === CompletenessForStatsStatusEnum.INCOMPLETE
  return { ...action, isIncomplete }
}
