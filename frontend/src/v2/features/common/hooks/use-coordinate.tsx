interface CoordinateHook {
  getCoords: (lat?: number, lng?: number) => [number?, number?]
  extractLatLngFromMultiPoint: (value?: string | unknown) => [number?, number?]
}

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
        lat = Number(latitude.toFixed(2))
        lng = Number(longitude.toFixed(2))
      }
    }
    return [lat, lng]
  }

  const extractObject = (multiPointString?: any): [number?, number?] => {
    try {
      const lng = multiPointString?.coordinates[0][0]
      const lat = multiPointString?.coordinates[0][1]

      return [Number(lat.toFixed(2)), Number(lng.toFixed(2))]
    } catch (e) {
      return [0, 0]
    }
  }

  const getCoords = (lat?: number, lng?: number): [number?, number?] => {
    return [lat ? Number(lat.toFixed(2)) : undefined, lng ? Number(lng.toFixed(2)) : undefined]
  }

  return {
    getCoords,
    extractLatLngFromMultiPoint
  }
}
