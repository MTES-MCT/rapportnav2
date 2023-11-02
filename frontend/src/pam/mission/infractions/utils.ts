import { ControlType } from '../../mission-types'

export const infractionTitleForControlType = (controlType: ControlType): string => {
  switch (controlType) {
    case ControlType.ADMINISTRATIVE:
      return 'Infraction administrative'
    case ControlType.NAVIGATION:
      return 'Infraction règles de navigation'
    case ControlType.SECURITY:
      return 'Infraction equipements et respect des normes de sécurité'
    case ControlType.GENS_DE_MER:
      return 'Infraction administrative gens de mer'
    default:
      return ''
  }
}
