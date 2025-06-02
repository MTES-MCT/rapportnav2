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
