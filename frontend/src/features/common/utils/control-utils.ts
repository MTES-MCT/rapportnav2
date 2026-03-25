import { ControlMethod, ControlType } from '@common/types/control-types'
import { VesselTypeEnum } from '@common/types/mission-types'

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

export const vesselTypeToHumanString = (vesselType?: VesselTypeEnum | null): string => {
  switch (vesselType) {
    case VesselTypeEnum.FISHING:
      return 'Navire de pêche professionnelle'
    case VesselTypeEnum.COMMERCIAL:
      return 'Navire de commerce'
    case VesselTypeEnum.MOTOR:
      return 'Navire de service'
    case VesselTypeEnum.SAILING:
      return 'Navire de plaisance professionnelle'
    case VesselTypeEnum.SAILING_LEISURE:
      return 'Navire de plaisance de loisir'
    case VesselTypeEnum.SHELLFISH:
      return 'Navire conchylicole'
    default:
      return ''
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
