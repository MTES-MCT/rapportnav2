import { renderHook } from '@testing-library/react'
import { isEqual } from 'lodash'
import { useCoordinate } from '../use-coordinate'

describe('useCoordinate', () => {
  it('should return undefined when coordinates is 0', () => {
    const { result } = renderHook(() => useCoordinate())
    expect(result.current.getCoords(0, 0)).toEqual([undefined, undefined])
  })

  it('should return coords without rounding it to 2 digits', () => {
    const { result } = renderHook(() => useCoordinate())
    expect(result.current.getCoords(15.44577889, 9.3266789)).toEqual([15.44577889, 9.3266789])
  })

  it('should return coords without rounding it to 2 digits', () => {
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
    ).toEqual([48.57504343, -1.98340431])
  })

  describe('rounded coordinate equality for real production drift (action 20905)', () => {
    it('treats the raw float-drifted coordinate as equal to its rounded form, unlike plain equality', () => {
      const ACTION_20905_LATITUDE = 43.412499999999966
      const ACTION_20905_LONGITUDE = 3.9366669999999995
      const { result } = renderHook(() => useCoordinate())
      const { getCoordRounded, isCoordsEqual } = result.current

      const rawCoords = [ACTION_20905_LATITUDE, ACTION_20905_LONGITUDE]
      const roundedCoords = getCoordRounded(ACTION_20905_LATITUDE, ACTION_20905_LONGITUDE)
      expect(isEqual(rawCoords, roundedCoords)).toBe(false)
      expect(isCoordsEqual(rawCoords, roundedCoords)).toBe(true)
    })
  })
})
