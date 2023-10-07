import { ControlMethod, VesselType } from '../../mission-types'

export const CONTROL_MULTIRADIO_OPTIONS = [
  {
    label: 'Oui',
    value: true
  },
  {
    label: 'Non',
    value: false
  },
  {
    label: 'Non contrôlé',
    value: null
  }
]

export const controlMethodToHumanString = (controlMethod: ControlMethod): String => {
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

export const vesselTypeToHumanString = (vesselType: VesselType): String => {
  switch (vesselType) {
    case VesselType.FISHING:
      return 'Navire de pêche professionnelle'
    case VesselType.COMMERCIAL:
      return 'Navire de commerce'
    case VesselType.MOTOR:
      return 'Navire de service'
    case VesselType.SAILING:
      return 'Navire de plaisance professionnelle'
    case VesselType.SAILING:
      return 'Navire de plaisance de loisir'
    default:
      return ''
  }
}
