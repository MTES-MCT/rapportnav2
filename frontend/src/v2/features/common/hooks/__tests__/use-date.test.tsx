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
