import Text from '@common/components/ui/text'
import TextByCacem from '@common/components/ui/text-by-cacem'
import TextByCnsp from '@common/components/ui/text-by-cnsp'
import MissionTimelineIncompleteControlTag from '@common/components/ui/v2/mission-timeline-incomplete-control-tag'
import MissionTimelineItemCardTag from '@common/components/ui/v2/mission-timeline-item-card-tag'
import MissionTimelineItemCardTitle from '@common/components/ui/v2/mission-timeline-item-card-title'
import { Action, ActionControl, ActionRescue, ActionStatus } from '@common/types/action-types'
import { ControlType } from '@common/types/control-types'
import {
  actionTargetTypeLabels,
  ActionTypeEnum,
  EnvActionControl,
  FormattedControlPlan,
  MissionSourceEnum
} from '@common/types/env-mission-types'
import { FishAction, formatMissionActionTypeForHumans } from '@common/types/fish-mission-types'
import { vesselNameOrUnknown } from '@common/utils/action-utils'
import { controlMethodToHumanString, vesselTypeToHumanString } from '@common/utils/control-utils'
import { mapStatusToText, statusReasonToHumanString } from '@common/utils/status-utils'
import { Icon, IconProps, THEME } from '@mtes-mct/monitor-ui'
import { FunctionComponent } from 'react'

export type ActionTimeline = {
  dropdownText?: string
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
  isActionStatus?: boolean
  timeline?: ActionTimeline
  icon?: FunctionComponent<IconProps>
}

export type ActionRegistry = {
  [key in ActionTypeEnum]: ActionRegistryItem
}

const ACTION_REGISTRY: ActionRegistry = {
  [ActionTypeEnum.CONTROL]: {
    style: { backgroundColor: THEME.color.white, borderColor: THEME.color.lightGray },
    title: 'Contrôles',
    icon: Icon.ControlUnit,
    timeline: {
      dropdownText: 'Ajouter des contrôles',
      getCardTitle: (action?: Action) => {
        if (action?.source === MissionSourceEnum.MONITORENV) {
          const data = action?.data as unknown as EnvActionControl
          return (
            <MissionTimelineItemCardTitle
              text="Contrôle"
              bold={
                data && 'formattedControlPlans' in data && !!data?.formattedControlPlans?.length
                  ? data?.formattedControlPlans?.map((theme: FormattedControlPlan) => theme.theme).join(', ')
                  : 'environnement'
              }
            />
          )
        }
        if (action?.source === MissionSourceEnum.MONITORFISH) {
          const data = action?.data as unknown as FishAction
          return (
            <MissionTimelineItemCardTitle
              text={`${formatMissionActionTypeForHumans(data?.actionType)} - ${vesselNameOrUnknown(data?.vesselName)}`}
            />
          )
        }
        const data = action?.data as unknown as ActionControl
        return (
          <MissionTimelineItemCardTitle
            text="Contrôles"
            bold={`${controlMethodToHumanString(data?.controlMethod)} - ${vesselTypeToHumanString(data?.vesselType)}`}
          />
        )
      },
      getCardTag: (action: Action) => {
        const data = action?.data as unknown as { controlsToComplete?: ControlType[] }
        if (data?.controlsToComplete && data?.controlsToComplete?.length > 0) {
          return <MissionTimelineIncompleteControlTag nbrIncompleteControl={data?.controlsToComplete?.length} />
        }
        return <MissionTimelineItemCardTag tags={action.summaryTags} />
      },
      getCardFooter: (action?: Action) => {
        if (action?.source === MissionSourceEnum.MONITORENV) return <TextByCacem />
        if (action?.source === MissionSourceEnum.MONITORFISH) return <TextByCnsp />
      },
      getCardSubtitle: (action: Action) => {
        if (action.source !== MissionSourceEnum.MONITORENV) return <></>
        const data = action?.data as unknown as EnvActionControl
        const s = data.actionNumberOfControls && data.actionNumberOfControls > 1 ? 's' : ''
        return (
          <Text as="h3" weight="normal" color={THEME.color.slateGray}>
            <b>{s ? `${data.actionNumberOfControls} contrôle${s}` : 'Nombre de contrôles inconnu'}</b>
            &nbsp;
            {`réalisé${s} sur des cibles de type`}
            &nbsp;
            <b>
              {data?.actionTargetType
                ? actionTargetTypeLabels[data.actionTargetType]?.libelle?.toLowerCase()
                : 'inconnu'}
            </b>
          </Text>
        )
      }
    }
  },
  [ActionTypeEnum.SURVEILLANCE]: {
    style: { backgroundColor: '#e5e5eb', borderColor: THEME.color.lightGray },
    icon: Icon.Observation,
    title: 'Surveillance Environnement',
    timeline: {
      dropdownText: `Surveillance`,
      getCardTitle: (action?: Action) => {
        const data = action?.data as unknown as EnvActionControl
        return (
          <MissionTimelineItemCardTitle
            text="Surveillance"
            bold={
              data?.formattedControlPlans?.length
                ? data?.formattedControlPlans?.map((t: FormattedControlPlan) => t.theme).join(', ')
                : 'environnement marin'
            }
          />
        )
      },
      getCardFooter: () => {
        return <TextByCacem />
      }
    }
  },
  [ActionTypeEnum.STATUS]: {
    style: {
      minHeight: 0
    },
    title: 'Statut du navire',
    timeline: {
      getCardTitle: (action?: Action, isSelected?: boolean) => {
        const data = action?.data as unknown as ActionStatus
        return (
          <div style={{ display: 'flex', alignItems: 'center' }}>
            <Text
              as="h3"
              weight="medium"
              color={isSelected ? THEME.color.charcoal : THEME.color.slateGray}
              data-testid="timeline-item-status-description"
            >
              <b>{`${mapStatusToText(data?.status)} - début${
                !!data?.reason ? ' - ' + statusReasonToHumanString(data?.reason) : ''
              }`}</b>
              {!!data?.observations ? ' - ' + data?.observations : ''}
            </Text>
            <Icon.EditUnbordered size={20} color={THEME.color.slateGray} style={{ marginLeft: '0.5rem' }} />
          </div>
        )
      },
      dropdownText: 'Ajouter des contrôles',
      getCardFooter: (action?: Action, prevAction?: Action) => {
        const data = prevAction?.data as unknown as ActionStatus
        return (
          <>
            {!!data && (
              <Text as={'h3'} color={THEME.color.slateGray} fontStyle={'italic'}>
                {`${mapStatusToText(data?.status)} ${data?.reason ? `- ${statusReasonToHumanString(data?.reason)} ` : ''}- fin`}
              </Text>
            )}
          </>
        )
      }
    },
    isActionStatus: true
  },
  [ActionTypeEnum.NOTE]: {
    style: { backgroundColor: THEME.color.blueYonder25, borderColor: THEME.color.lightGray },
    title: 'Note libre',
    icon: Icon.Note,
    timeline: {
      dropdownText: 'Ajouter une note libre',
      getCardTitle: () => <MissionTimelineItemCardTitle text="Note libre" />
    }
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
    }
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
    }
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
      getCardTitle: (action?: Action) => {
        const data = action?.data as unknown as ActionRescue
        return (
          <MissionTimelineItemCardTitle
            truncate
            text="Assistance /"
            bold={`${data?.isPersonRescue ? 'Sauvegarde de la vie humaine' : 'Assistance de navire en difficulté'}`}
          />
        )
      }
    }
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
    }
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
    }
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
    }
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
    }
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
    }
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
      getCardTitle: () => {
        return <></>
      },
      dropdownText: `Contact`
    }
  }
}

export function useActionRegistry(actionType: ActionTypeEnum): ActionRegistryItem {
  const getAction = () => ACTION_REGISTRY[actionType]
  const action = getAction()
  return { ...action }
}
