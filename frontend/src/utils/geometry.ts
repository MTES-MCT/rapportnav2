// TODO this should probably handled by robust library instead and a proper usage of geometric data in general
export function extractLatLonFromMultiPoint(multiPointString?: string): [number, number] | null {
  // Regular expression to match coordinates within "MULTIPOINT" string
  const regex = /MULTIPOINT \(\(([-\d.]+) ([-\d.]+)\)\)/

  // Attempt to match the regular expression
  const match = multiPointString?.match(regex)

  if (match && match.length === 3) {
    // Extracted coordinates from the match
    const latitude = parseFloat(match[2])
    const longitude = parseFloat(match[1])

    // Check if the parsed values are valid numbers
    if (!isNaN(latitude) && !isNaN(longitude)) {
      return [latitude, longitude]
    }
  }

  return null
}
