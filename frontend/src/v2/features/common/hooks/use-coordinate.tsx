interface CoordinateHook {
  getCoords: (lat?: number, lng?: number) => [number?, number?]
  extractLatLngFromMultiPoint: (value?: string) => [number?, number?]
}

export function useCoordinate(): CoordinateHook {
  const extractLatLngFromMultiPoint = (multiPointString?: string): [number?, number?] => {
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

  const getCoords = (lat?: number, lng?: number): [number?, number?] => {
    return [lat ? Number(lat.toFixed(2)) : 0, lng ? Number(lng.toFixed(2)) : 0]
  }

  return {
    getCoords,
    extractLatLngFromMultiPoint
  }
}
