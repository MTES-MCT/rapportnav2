import { VesselSizeEnum, VesselTypeEnum } from '../../env-mission-types'
import { ControlMethod, ControlType, VesselSize, VesselType } from '../../mission-types'

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

export const vesselTypeToHumanString = (vesselType?: VesselType | VesselTypeEnum | null): string => {
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

export const vesselSizeToHumanString = (vesselSize?: VesselSize | VesselSizeEnum | null): string => {
  switch (vesselSize) {
    case VesselSize.LESS_THAN_12m:
      return 'Moins de 12m'
    case VesselSize.FROM_12_TO_24m:
      return 'Entre 12m et 24m'
    case VesselSize.FROM_24_TO_46m:
      return 'Entre 24m et 46m'
    case VesselSize.MORE_THAN_46m:
      return 'Plus de 46m'
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
