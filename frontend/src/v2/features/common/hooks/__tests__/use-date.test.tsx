import { UTCDate } from '@date-fns/utc'
import { renderHook } from '@testing-library/react'
import { useDate } from '../use-date'

describe('useDate', () => {
  it('should return undefined date is null or undefined for preprocessDateForPicker', () => {
    const { result } = renderHook(() => useDate())
    expect(result.current.preprocessDateForPicker(null)).toEqual(undefined)
    expect(result.current.preprocessDateForPicker(undefined)).toEqual(undefined)
  })

  it('should return a normal value for nomal string for preprocessDateForPicker', () => {
    const { result } = renderHook(() => useDate())
    const date = result.current.preprocessDateForPicker('2024-09-13T15:24:00Z')

    expect(date).not.toBeNull()
    expect(date).not.toBeUndefined()
    expect(date?.toDateString()).toEqual('Fri Sep 13 2024')
  })

  it('should return mission ulam date type', () => {
    const { result } = renderHook(() => useDate())
    const date = result.current.formaDateMissionNameUlam('2024-09-13T15:24:00Z')
    expect(date).not.toBeNull()
    expect(date).not.toBeUndefined()
    expect(date).toEqual('2024-09')
  })

  it('should return undefined date is null or undefined for postprocessDateFromPicker', () => {
    const { result } = renderHook(() => useDate())
    expect(result.current.postprocessDateFromPicker(null)).toEqual(undefined)
    expect(result.current.postprocessDateFromPicker(undefined)).toEqual(undefined)
  })

  it('should return a normal value for nomal string for postprocessDateFromPicker', () => {
    const { result } = renderHook(() => useDate())
    const date = result.current.postprocessDateFromPicker(new Date('2024-09-13T15:24:00Z'))

    expect(date).not.toBeNull()
    expect(date).not.toBeUndefined()
    expect(new Date(date!!).toDateString()).toEqual('Fri Sep 13 2024')
  })
})
it('should return correct month range for getTodayMonthRange with provided date', () => {
  const { result } = renderHook(() => useDate())
  const date = new UTCDate('2024-03-15T12:00:00Z')
  const range = result.current.getTodayMonthRange(date)
  expect(range.startDateTimeUtc).toBe(new UTCDate('2024-03-01T00:00:00.000Z').toISOString())
  expect(range.endDateTimeUtc).toBe(new UTCDate('2024-03-31T23:59:59.999Z').toISOString())
})

it('should return correct year range for getTodayYearRange with provided date', () => {
  const { result } = renderHook(() => useDate())
  const date = new UTCDate('2024-03-15T12:00:00Z')
  const range = result.current.getTodayYearRange(date)
  expect(range.startDateTimeUtc).toBe(new UTCDate('2024-01-01T00:00:00.000Z').toISOString())
  expect(range.endDateTimeUtc).toBe(new Date('2024-12-31T23:59:59.999Z').toISOString())
})

it('should return current month range for getTodayMonthRange with no date', () => {
  const { result } = renderHook(() => useDate())
  const now = new Date()
  const expectedStart = new Date(Date.UTC(now.getUTCFullYear(), now.getUTCMonth(), 1, 0, 0, 0, 0)).toISOString()
  const expectedEnd = new Date(Date.UTC(now.getUTCFullYear(), now.getUTCMonth() + 1, 0, 0, 0, 0, 0)).toISOString()
  const range = result.current.getTodayMonthRange()
  expect(range.startDateTimeUtc).toBe(expectedStart)
  expect([
    expectedEnd,
    new Date(Date.UTC(now.getUTCFullYear(), now.getUTCMonth() + 1, 0, 23, 59, 59, 999)).toISOString()
  ]).toContain(range.endDateTimeUtc)
})

it('should return current year range for getTodayYearRange with no date', () => {
  const { result } = renderHook(() => useDate())
  const now = new Date()
  const expectedStart = new Date(Date.UTC(now.getUTCFullYear(), 0, 1, 0, 0, 0, 0)).toISOString()
  const expectedEnd = new Date(Date.UTC(now.getUTCFullYear(), 11, 31, 0, 0, 0, 0)).toISOString()
  const range = result.current.getTodayYearRange()
  expect(range.startDateTimeUtc).toBe(expectedStart)
  expect([expectedEnd, new Date(Date.UTC(now.getUTCFullYear(), 11, 31, 23, 59, 59, 999)).toISOString()]).toContain(
    range.endDateTimeUtc
  )
})

it('formatInquiryName returns correct string', () => {
  const { result } = renderHook(() => useDate())
  expect(result.current.formatInquiryName('2024-09-13T15:24:00Z')).toBe('Contrôle croisé n°2024-09-13')
  expect(result.current.formatInquiryName(undefined)).toBe('Contrôle croisé n°--/--/----')
})

describe('getDateRangeForInput / getDateRangeFromInput round trip', () => {
  it('round-trips a millisecond-less ISO string unchanged', () => {
    const { result } = renderHook(() => useDate())
    const { getDateRangeForInput, getDateRangeFromInput } = result.current

    const raw = { startDateTimeUtc: '2026-05-13T16:00:00Z', endDateTimeUtc: '2026-05-13T18:00:00Z' }

    const dates = getDateRangeForInput(raw)
    expect(dates[0]).toBe(raw.startDateTimeUtc)
    expect(typeof dates[0]).toBe('string')

    const roundTripped = getDateRangeFromInput(dates)
    expect(roundTripped.startDateTimeUtc).toBe(raw.startDateTimeUtc)
    expect(roundTripped.endDateTimeUtc).toBe(raw.endDateTimeUtc)
  })

  it('strips padding-only ".000" milliseconds added by toISOString', () => {
    const { result } = renderHook(() => useDate())
    const { getDateRangeFromInput } = result.current

    const roundTripped = getDateRangeFromInput([new Date('2026-05-13T16:00:00.000Z'), undefined])
    expect(roundTripped.startDateTimeUtc).toBe('2026-05-13T16:00:00Z')
  })

  it('preserves a genuine non-zero millisecond value instead of stripping it as padding', () => {
    const { result } = renderHook(() => useDate())
    const { getDateRangeForInput, getDateRangeFromInput } = result.current

    const raw = { startDateTimeUtc: '2025-07-28T07:43:46.297Z', endDateTimeUtc: null }
    const dates = getDateRangeForInput(raw)
    const roundTripped = getDateRangeFromInput(dates)
    expect(roundTripped.startDateTimeUtc).toBe('2025-07-28T07:43:46.297Z')
  })
})
