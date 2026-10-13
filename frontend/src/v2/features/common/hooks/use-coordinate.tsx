import { isEqual } from 'lodash'

interface CoordinateHook {
  getCoords: (lat?: number, lng?: number) => [number?, number?]
  getCoordRounded: (lat?: number, lng?: number) => [number?, number?]
  extractLatLngFromMultiPoint: (value?: string | unknown) => [number?, number?]
  extractLatLngFromMultiPointRounded: (value?: string | unknown) => [number?, number?]
  roundCoord: (value?: number) => number | undefined
  isCoordsEqual: (a?: (number | undefined)[], b?: (number | undefined)[]) => boolean
}

export const COORD_PRECISION = 3

export const roundCoord = (value?: number): number | undefined =>
  value !== undefined ? Number(value.toFixed(COORD_PRECISION)) : value

export const isCoordsEqual = (a?: (number | undefined)[], b?: (number | undefined)[]): boolean =>
  isEqual(roundCoord(a?.[0]), roundCoord(b?.[0])) && isEqual(roundCoord(a?.[1]), roundCoord(b?.[1]))

export function useCoordinate(): CoordinateHook {
  const extractLatLngFromMultiPoint = (multiPointString?: string | unknown): [number?, number?] => {
    if (typeof multiPointString === 'string') {
      return extracString(multiPointString)
    } else {
      return extractObject(multiPointString)
    }
  }

  const extracString = (multiPointString?: string): [number?, number?] => {
    let lat = undefined
    let lng = undefined
    const regex = /MULTIPOINT \(\(([-\d.]+) ([-\d.]+)\)\)/
    const match = multiPointString?.match(regex)
    if (match && match.length === 3) {
      const latitude = parseFloat(match[2])
      const longitude = parseFloat(match[1])
      if (!isNaN(latitude) && !isNaN(longitude)) {
        lat = Number(latitude)
        lng = Number(longitude)
      }
    }
    return [lat, lng]
  }

  const extractObject = (multiPointString?: any): [number?, number?] => {
    try {
      const lng = multiPointString?.coordinates[0][0]
      const lat = multiPointString?.coordinates[0][1]

      return [Number(lat), Number(lng)]
    } catch (e) {
      return [0, 0]
    }
  }

  const getCoords = (lat?: number, lng?: number): [number?, number?] => {
    return [lat ? Number(lat) : undefined, lng ? Number(lng) : undefined]
  }

  const getCoordRounded = (lat?: number, lng?: number): [number?, number?] =>
    getCoords(lat, lng).map(roundCoord) as [number?, number?]

  const extractLatLngFromMultiPointRounded = (multiPointString?: string | unknown): [number?, number?] =>
    extractLatLngFromMultiPoint(multiPointString).map(roundCoord) as [number?, number?]

  return {
    getCoords,
    roundCoord,
    isCoordsEqual,
    getCoordRounded,
    extractLatLngFromMultiPoint,
    extractLatLngFromMultiPointRounded
  }
}
