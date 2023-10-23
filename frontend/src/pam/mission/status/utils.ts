import { ActionStatusType } from '../../mission-types'
import { THEME } from '@mtes-mct/monitor-ui'

export const getColorForStatus = (status: ActionStatusType) => {
  switch (status) {
    case ActionStatusType.NAVIGATING:
      return '#52B0FF'
    case ActionStatusType.ANCHORED:
      //   return THEME.color.blueYonder
      return '#567A9E'
    case ActionStatusType.DOCKED:
      return THEME.color.goldenPoppy
    case ActionStatusType.UNAVAILABLE:
      return THEME.color.maximumRed
    case ActionStatusType.UNKNOWN:
    default:
      return 'transparent'
  }
}

export const mapStatusToText = (status: ActionStatusType) => {
  switch (status) {
    case ActionStatusType.NAVIGATING:
      return 'Navigation'
    case ActionStatusType.ANCHORED:
      return 'Mouillage'
    case ActionStatusType.DOCKED:
      return 'Présence à quai'
    case ActionStatusType.UNAVAILABLE:
      return 'Indisponibilité'
    case ActionStatusType.UNKNOWN:
    default:
      return 'Inconnu'
  }
}
