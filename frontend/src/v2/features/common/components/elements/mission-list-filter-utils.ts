// The three enum filters carried in the URL for the mission list (multi-select, repeated params).
export const MISSION_LIST_FILTER_KEYS = ['statuses', 'completenessStatuses', 'reportTypes']

/** Whether any mission-list filter is active: one of the three enum filters, or a date range (`dateMode`). */
export const hasActiveMissionListFilters = (searchParams: URLSearchParams): boolean =>
  MISSION_LIST_FILTER_KEYS.some(key => searchParams.getAll(key).length > 0) || searchParams.get('dateMode') !== null

/** New params with every mission-list filter (enum filters + date range) cleared. */
export const clearMissionListFilters = (searchParams: URLSearchParams): URLSearchParams => {
  const next = new URLSearchParams(searchParams)
  MISSION_LIST_FILTER_KEYS.forEach(key => next.delete(key))
  next.delete('dateMode')
  next.delete('startDateTimeUtc')
  next.delete('endDateTimeUtc')
  return next
}
