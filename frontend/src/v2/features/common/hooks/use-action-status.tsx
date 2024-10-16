import { ActionStatusType } from '@common/types/action-types'
import { THEME } from '@mtes-mct/monitor-ui'

type Component = {
  text: string
  color: string
}

type StatusComponent = { [key in ActionStatusType]: Component }

const ACTION_STATUS_REGISTRY: StatusComponent = {
  [ActionStatusType.NAVIGATING]: {
    color: THEME.color.blueGray,
    text: 'Navigation'
  },
  [ActionStatusType.ANCHORED]: {
    color: THEME.color.blueYonder,
    text: 'Mouillage'
  },
  [ActionStatusType.DOCKED]: {
    color: THEME.color.goldenPoppy,
    text: 'Présence à quai'
  },
  [ActionStatusType.UNAVAILABLE]: {
    color: THEME.color.maximumRed,
    text: 'Indisponibilité'
  },
  [ActionStatusType.UNKNOWN]: {
    color: 'transparent',
    text: 'Inconnu'
  }
}

interface ActionStatusHook {
  text: string
  color: string
}

export function useActionStatus(actionStatusType: ActionStatusType): ActionStatusHook {
  return ACTION_STATUS_REGISTRY[actionStatusType]
}
