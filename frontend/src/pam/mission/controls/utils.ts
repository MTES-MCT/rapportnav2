import { ControlMethod, ControlType, VesselType } from '../../mission-types'

export const controlMethodToHumanString = (controlMethod?: ControlMethod | null): string => {
  switch (controlMethod) {
    case ControlMethod.AIR:
      return 'aérien'
    case ControlMethod.LAND:
      return 'à Terre'
    case ControlMethod.SEA:
      return 'en Mer'
    default:
      return ''
  }
}

export const vesselTypeToHumanString = (vesselType?: VesselType | null): string => {
  switch (vesselType) {
    case VesselType.FISHING:
      return 'Navire de pêche professionnelle'
    case VesselType.COMMERCIAL:
      return 'Navire de commerce'
    case VesselType.MOTOR:
      return 'Navire de service'
    case VesselType.SAILING:
      return 'Navire de plaisance professionnelle'
    case VesselType.SAILING_LEISURE:
      return 'Navire de plaisance de loisir'
    default:
      return ''
  }
}

export const controlTitle = (controlType: ControlType) => {
  switch (controlType) {
    case ControlType.ADMINISTRATIVE:
      return 'Contrôle administratif navire'
    case ControlType.GENS_DE_MER:
      return 'Contrôle administratif gens de mer'
    case ControlType.SECURITY:
      return 'Equipements et respect des normes de sécurité'
    case ControlType.NAVIGATION:
      return 'Respect des règles de navigation'
  }
}

export function getDisabledControlTypes(enabledControlTypes?: ControlType[]): ControlType[] {
  const allControlTypes = Object.values(ControlType)
  if (!enabledControlTypes) {
    return allControlTypes
  }
  const disabledControlTypes = allControlTypes.filter(controlType => !enabledControlTypes.includes(controlType))
  return disabledControlTypes
}
