import {ActionStatusReason, ActionStatusType} from '../../../types/action-types'
import {THEME} from '@mtes-mct/monitor-ui'

export const getColorForStatus = (status: ActionStatusType) => {
    switch (status) {
        case ActionStatusType.NAVIGATING:
            return THEME.color.blueGray
        case ActionStatusType.ANCHORED:
            return THEME.color.blueYonder
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

export const statusReasonToHumanString = (reason?: ActionStatusReason): string | undefined => {
    switch (reason) {
        case ActionStatusReason.MAINTENANCE:
            return 'Maintenance'
        case ActionStatusReason.WEATHER:
            return 'Météo'
        case ActionStatusReason.REPRESENTATION:
            return 'Représentation'
        case ActionStatusReason.ADMINISTRATION:
            return 'Administration'
        case ActionStatusReason.HARBOUR_CONTROL:
            return 'Contrôle portuaire'
        case ActionStatusReason.TECHNICAL:
            return 'Technique'
        case ActionStatusReason.PERSONNEL:
            return 'Personnel'
        case ActionStatusReason.OTHER:
            return 'Autre'
        default:
            return undefined
    }
}