import { ControlType } from '../../../common/types/control-types.ts'

export const infractionTitleForControlType = (controlType: ControlType): string => {
  switch (controlType) {
    case ControlType.ADMINISTRATIVE:
      return 'Infraction administrative'
    case ControlType.NAVIGATION:
      return 'Infraction règles de navigation'
    case ControlType.SECURITY:
      return 'Infraction équipements et respect des normes de sécurité'
    case ControlType.GENS_DE_MER:
      return 'Infraction administrative gens de mer'
    default:
      return ''
  }
}
export const infractionButtonTitle = (controlType: ControlType): string => {
  switch (controlType) {
    case ControlType.ADMINISTRATIVE:
      return 'Ajouter une infraction administrative'
    case ControlType.NAVIGATION:
      return 'Ajouter une infraction règle de navigation'
    case ControlType.SECURITY:
      return 'Ajouter une infraction sécurité'
    case ControlType.GENS_DE_MER:
      return 'Ajouter une infraction administrative'
    default:
      return ''
  }
}
