import {
  clearMissionListFilters,
  hasActiveMissionListFilters,
  MISSION_LIST_FILTER_KEYS
} from '../mission-list-filter-utils.ts'

describe('hasActiveMissionListFilters', () => {
  test('is false when there are no filters', () => {
    expect(hasActiveMissionListFilters(new URLSearchParams())).toBe(false)
  })

  test('is true when a status / completeness / report-type filter is set', () => {
    expect(hasActiveMissionListFilters(new URLSearchParams('statuses=ENDED'))).toBe(true)
    expect(hasActiveMissionListFilters(new URLSearchParams('completenessStatuses=VALID'))).toBe(true)
    expect(hasActiveMissionListFilters(new URLSearchParams('reportTypes=FIELD_REPORT'))).toBe(true)
  })

  test('is true when a date range (dateMode) is selected', () => {
    expect(hasActiveMissionListFilters(new URLSearchParams('dateMode=CURRENT_MONTH'))).toBe(true)
  })

  test('ignores unrelated params (paging)', () => {
    expect(hasActiveMissionListFilters(new URLSearchParams('offset=15&limit=15'))).toBe(false)
  })
})

describe('clearMissionListFilters', () => {
  test('removes every filter + date param but keeps unrelated ones', () => {
    const params = new URLSearchParams()
    params.append('statuses', 'ENDED')
    params.append('statuses', 'IN_PROGRESS')
    params.set('completenessStatuses', 'VALID')
    params.set('reportTypes', 'FIELD_REPORT')
    params.set('dateMode', 'CUSTOM')
    params.set('startDateTimeUtc', '2025-01-01T00:00:00Z')
    params.set('endDateTimeUtc', '2025-02-01T00:00:00Z')
    params.set('somethingElse', 'keep')

    const cleared = clearMissionListFilters(params)

    MISSION_LIST_FILTER_KEYS.forEach(key => expect(cleared.getAll(key)).toEqual([]))
    expect(cleared.get('dateMode')).toBeNull()
    expect(cleared.get('startDateTimeUtc')).toBeNull()
    expect(cleared.get('endDateTimeUtc')).toBeNull()
    expect(cleared.get('somethingElse')).toBe('keep')
  })

  test('does not mutate the input params', () => {
    const params = new URLSearchParams('statuses=ENDED')
    clearMissionListFilters(params)
    expect(params.get('statuses')).toBe('ENDED')
  })
})
