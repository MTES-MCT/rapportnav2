interface CoordinateHook {
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
        lat = latitude
        lng = longitude
      }
    }
    return [lat, lng]
  }

  return {
    extractLatLngFromMultiPoint
  }
}
