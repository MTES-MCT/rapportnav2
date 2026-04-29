import { cleanLocationFields } from '../location-fields-cleaner'
import { LocationType } from '../../types/location-type'

describe('cleanLocationFields', () => {
  const geoCoords = [48.85, 2.35]
  const data = { city: 'Paris', zipCode: '75000', portLocode: 'FRPAR' }

  it('should return lat/lon and clear city/zipCode/portLocode for GPS', () => {
    const result = cleanLocationFields(LocationType.GPS, geoCoords, data)
    expect(result).toEqual({
      latitude: 48.85,
      longitude: 2.35,
      city: undefined,
      zipCode: undefined,
      portLocode: undefined
    })
  })

  it('should return city/zipCode and clear lat/lon/portLocode for COMMUNE', () => {
    const result = cleanLocationFields(LocationType.COMMUNE, geoCoords, data)
    expect(result).toEqual({
      latitude: undefined,
      longitude: undefined,
      city: 'Paris',
      zipCode: '75000',
      portLocode: undefined
    })
  })

  it('should return portLocode and clear lat/lon/city/zipCode for PORT', () => {
    const result = cleanLocationFields(LocationType.PORT, geoCoords, data)
    expect(result).toEqual({
      latitude: undefined,
      longitude: undefined,
      city: undefined,
      zipCode: undefined,
      portLocode: 'FRPAR'
    })
  })

  it('should return lat/lon only for undefined locationType (default)', () => {
    const result = cleanLocationFields(undefined, geoCoords, data)
    expect(result).toEqual({ latitude: 48.85, longitude: 2.35 })
    expect(result).not.toHaveProperty('city')
    expect(result).not.toHaveProperty('zipCode')
    expect(result).not.toHaveProperty('portLocode')
  })

  it('should handle undefined geoCoords entries for GPS', () => {
    const result = cleanLocationFields(LocationType.GPS, [undefined, undefined], {})
    expect(result).toEqual({
      latitude: undefined,
      longitude: undefined,
      city: undefined,
      zipCode: undefined,
      portLocode: undefined
    })
  })

  it('should handle missing data fields for COMMUNE', () => {
    const result = cleanLocationFields(LocationType.COMMUNE, geoCoords, {})
    expect(result).toEqual({
      latitude: undefined,
      longitude: undefined,
      city: undefined,
      zipCode: undefined,
      portLocode: undefined
    })
  })

  it('should handle missing portLocode for PORT', () => {
    const result = cleanLocationFields(LocationType.PORT, [], {})
    expect(result).toEqual({
      latitude: undefined,
      longitude: undefined,
      city: undefined,
      zipCode: undefined,
      portLocode: undefined
    })
  })

  it('should handle empty geoCoords for default case', () => {
    const result = cleanLocationFields(undefined, [], {})
    expect(result).toEqual({ latitude: undefined, longitude: undefined })
  })
})
