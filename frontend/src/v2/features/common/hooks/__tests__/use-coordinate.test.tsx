import { renderHook } from '@testing-library/react'
import { useCoordinate } from '../use-coordinate'

describe('useCoordinate', () => {
  it('should return undefined when coordinates is 0', () => {
    const { result } = renderHook(() => useCoordinate())
    expect(result.current.getCoords(0, 0)).toEqual([undefined, undefined])
  })

  it('should return coords with 2 digits only', () => {
    const { result } = renderHook(() => useCoordinate())
    expect(result.current.getCoords(15.44577889, 9.3266789)).toEqual([15.45, 9.33])
  })

  it('should return coords with 2 digits only', () => {
    const { result } = renderHook(() => useCoordinate())
    expect(
      result.current.extractLatLngFromMultiPoint({
        type: 'MultiPoint',
        coordinates: [[-1.98340431, 48.57504343]],
        crs: {
          type: 'name',
          properties: {
            name: 'EPSG:4326'
          }
        }
      })
    ).toEqual([48.58, -1.98])
  })
})
