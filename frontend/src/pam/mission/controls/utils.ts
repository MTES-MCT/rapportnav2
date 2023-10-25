import {
  ControlAdministrative,
  ControlGensDeMer,
  ControlMethod,
  ControlNavigation,
  ControlSecurity,
  VesselType
} from '../../mission-types'

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
    case VesselType.SAILING_LEISURE:
      return 'Navire de plaisance de loisir'
    default:
      return ''
  }
}

export const controlIsEnabled = (
  controlData?: ControlAdministrative | ControlSecurity | ControlNavigation | ControlGensDeMer
) =>
  (!!controlData &&
    controlData !== null &&
    Object.keys(controlData).length > 0 &&
    controlData?.deletedAt === undefined) ||
  (!!controlData && controlData !== null && controlData?.deletedAt === null)
