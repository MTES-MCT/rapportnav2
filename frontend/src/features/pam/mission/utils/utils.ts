import { formatDateForMissionName } from '../../../common/utils/dates.ts'

export function groupByDay(obj: any[], dateField: string) {
  return obj.reduce((groupedObj, subObj) => {
    // Extract day from startDateTimeUtc
    const day = new Date(subObj[dateField]).toLocaleDateString()

    // Create a group for the day if it doesn't exist
    groupedObj[day] = groupedObj[day] || []

    // Add the subObj to the corresponding day
    groupedObj[day].push(subObj)

    return groupedObj
  }, {})
}

export const formatMissionName = (startDate?: string): string => {
  return `Mission #${formatDateForMissionName(startDate)}`
}
