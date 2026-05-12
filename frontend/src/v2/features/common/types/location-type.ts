export enum LocationType {
  GPS = 'GPS',
  PORT = 'PORT',
  COMMUNE = 'COMMUNE'
}

export const LOCATION_TYPES: Record<LocationType, string> = {
  [LocationType.GPS]: 'Coordonnees GPS',
  [LocationType.PORT]: 'Port',
  [LocationType.COMMUNE]: 'Commune'
}

export const SEA_LOCATION_TYPES = [LocationType.GPS, LocationType.PORT, LocationType.COMMUNE]
export const LAND_LOCATION_TYPES = [LocationType.PORT, LocationType.COMMUNE]
