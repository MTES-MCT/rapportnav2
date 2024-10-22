import { Action } from '@common/types/action-types'
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
import MissionTimelineItemControlCardFooter from '../../mission-timeline/components/elements/mission-timeline-item-control-card-footer'
import MissionTimelineItemControlCardSubtitle from '../../mission-timeline/components/elements/mission-timeline-item-control-card-Subtitle'
import MissionTimelineItemControlCardTag from '../../mission-timeline/components/elements/mission-timeline-item-control-card-tag'
import MissionTimelineItemControlCardTitle from '../../mission-timeline/components/elements/mission-timeline-item-control-card-title'
import MissionTimelineItemRescueCardTitle from '../../mission-timeline/components/elements/mission-timeline-item-rescue-card-title'
import MissionTimelineItemSruveillanceCardTitle from '../../mission-timeline/components/elements/mission-timeline-item-surveillance-card-title'
import MissionTimelineItemCardTitle from '../../mission-timeline/components/ui/mission-timeline-item-card-title'
import TextByCacem from '../components/ui/text-by-cacem'

export type ActionTimeline = {
  dropdownText?: string
  noPadding?: boolean
  getCardTag?: (action: Action) => JSX.Element | undefined
  getCardSubtitle?: (action: Action) => JSX.Element | undefined
  getCardFooter?: (action?: Action, prevAction?: Action) => JSX.Element | undefined
  getCardTitle: (action?: Action, isSelected?: boolean) => JSX.Element | undefined
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
  timeline?: ActionTimeline
  hasStatusTag?: boolean
  icon?: FunctionComponent<IconProps>
  actionComponent?: FunctionComponent<{
    action: Action
    onChange: (newAction: Action) => void
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
      getCardTitle: (action?: Action) => <MissionTimelineItemControlCardTitle action={action} />,
      getCardTag: (action: Action) => <MissionTimelineItemControlCardTag action={action} />,
      getCardFooter: (action?: Action) => <MissionTimelineItemControlCardFooter action={action} />,
      getCardSubtitle: (action: Action) => <MissionTimelineItemControlCardSubtitle action={action} />
    },
    actionComponent: MissionActionItemControl
  },
  [ActionTypeEnum.SURVEILLANCE]: {
    style: { backgroundColor: '#e5e5eb', borderColor: THEME.color.lightGray },
    icon: Icon.Observation,
    title: 'Surveillance Environnement',
    timeline: {
      dropdownText: `Surveillance`,
      getCardTitle: (action?: Action) => <MissionTimelineItemSruveillanceCardTitle action={action} />,
      getCardFooter: () => <TextByCacem />
    },
    actionComponent: MissionActionItemSurveillance
  },
  [ActionTypeEnum.NOTE]: {
    style: { backgroundColor: THEME.color.blueYonder25, borderColor: THEME.color.lightGray },
    title: 'Note libre',
    icon: Icon.Note,
    timeline: {
      dropdownText: 'Ajouter une note libre',
      getCardTitle: () => <MissionTimelineItemCardTitle text="Note libre" />
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
      getCardTitle: () => <MissionTimelineItemCardTitle text="Permanence Vigimer" />
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
      getCardTitle: () => <MissionTimelineItemCardTitle text="Manifestation nautique" />
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
      getCardTitle: (action?: Action) => <MissionTimelineItemRescueCardTitle action={action} />
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
      getCardTitle: () => <MissionTimelineItemCardTitle text="Représentation" />
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
      getCardTitle: () => <MissionTimelineItemCardTitle text="Maintien de l'ordre public" />
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
      getCardTitle: () => <MissionTimelineItemCardTitle text="Opération de lutte anti-pollution" />
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
      getCardTitle: () => <MissionTimelineItemCardTitle text="Permanence BAAEM" />
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
      getCardTitle: () => <MissionTimelineItemCardTitle text="Lutte contre l'immigration illégale" />
    },
    actionComponent: MissionActionItemIllegalImmigration
  },
  [ActionTypeEnum.OTHER]: {
    style: {},
    icon: Icon.More,
    timeline: {
      dropdownText: 'Ajouter une autre activité de mission',
      getCardTitle: () => <MissionTimelineItemCardTitle text="Ajouter une autre activité de mission" />
    }
  },
  [ActionTypeEnum.CONTACT]: {
    style: {},
    timeline: {
      getCardTitle: () => <></>,
      dropdownText: `Contact`
    }
  }
}

export type ActionRegistryHook = {
  isIncomplete: (action: Action) => boolean
} & ActionRegistryItem

export function useActionRegistry(actionType: ActionTypeEnum): ActionRegistryHook {
  const getAction = () => ACTION_REGISTRY[actionType]
  const action = getAction()
  const isIncomplete = (action?: Action) =>
    action?.completenessForStats.status === CompletenessForStatsStatusEnum.INCOMPLETE
  return { ...action, isIncomplete }
}
