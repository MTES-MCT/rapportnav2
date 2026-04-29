import { LocationType } from '../types/location-type'

interface LocationFields {
  latitude?: number
  longitude?: number
  city?: string
  zipCode?: string
  portLocode?: string
}

export function cleanLocationFields(
  locationType: LocationType | undefined,
  geoCoords: (number | undefined)[],
  data: LocationFields
): LocationFields {
  switch (locationType) {
    case LocationType.GPS:
      return { latitude: geoCoords[0], longitude: geoCoords[1], city: undefined, zipCode: undefined, portLocode: undefined }
    case LocationType.COMMUNE:
      return { latitude: undefined, longitude: undefined, city: data.city, zipCode: data.zipCode, portLocode: undefined }
    case LocationType.PORT:
      return { latitude: undefined, longitude: undefined, city: undefined, zipCode: undefined, portLocode: data.portLocode }
    default:
      return { latitude: geoCoords[0], longitude: geoCoords[1] }
  }
}
