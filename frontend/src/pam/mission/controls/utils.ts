import { VesselSizeEnum, VesselTypeEnum } from '../../../types/env-mission-types'
import { ControlMethod, ControlType } from '../../../types/control-types'

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
    default:
      return ''
  }
}

export const VESSEL_TYPE_OPTIONS = [
  {
    label: vesselTypeToHumanString(VesselTypeEnum.FISHING),
    value: VesselTypeEnum.FISHING
  },
  {
    label: vesselTypeToHumanString(VesselTypeEnum.SAILING),
    value: VesselTypeEnum.SAILING
  },
  {
    label: vesselTypeToHumanString(VesselTypeEnum.MOTOR),
    value: VesselTypeEnum.MOTOR
  },
  {
    label: vesselTypeToHumanString(VesselTypeEnum.COMMERCIAL),
    value: VesselTypeEnum.COMMERCIAL
  },
  {
    label: vesselTypeToHumanString(VesselTypeEnum.SAILING_LEISURE),
    value: VesselTypeEnum.SAILING_LEISURE
  }
]

export const vesselSizeToHumanString = (vesselSize?: VesselSizeEnum | VesselSizeEnum | null): string => {
  switch (vesselSize) {
    case VesselSizeEnum.LESS_THAN_12m:
      return 'Moins de 12m'
    case VesselSizeEnum.FROM_12_TO_24m:
      return 'Entre 12m et 24m'
    case VesselSizeEnum.FROM_24_TO_46m:
      return 'Entre 24m et 46m'
    case VesselSizeEnum.MORE_THAN_46m:
      return 'Plus de 46m'
    default:
      return ''
  }
}

export const VESSEL_SIZE_OPTIONS = [
  {
    label: 'Moins de 12m',
    value: VesselSizeEnum.LESS_THAN_12m
  },
  {
    label: 'Entre 12m et 24m',
    value: VesselSizeEnum.FROM_12_TO_24m
  },
  {
    label: 'Entre 24m et 46m',
    value: VesselSizeEnum.FROM_24_TO_46m
  },
  {
    label: 'Plus de 46m',
    value: VesselSizeEnum.MORE_THAN_46m
  }
]

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

export const CONTROL_TYPE_OPTIONS = [
  {
    label: `Infraction - ${controlTitle(ControlType.ADMINISTRATIVE)}`,
    value: ControlType.ADMINISTRATIVE
  },
  {
    label: `Infraction - ${controlTitle(ControlType.SECURITY)}`,
    value: ControlType.SECURITY
  },
  {
    label: `Infraction - ${controlTitle(ControlType.NAVIGATION)}`,
    value: ControlType.NAVIGATION
  },
  {
    label: `Infraction - ${controlTitle(ControlType.GENS_DE_MER)}`,
    value: ControlType.GENS_DE_MER
  }
]
