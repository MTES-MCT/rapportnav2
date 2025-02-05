import { renderHook } from '@testing-library/react'
import { useDate } from '../use-date'

describe('useDate', () => {
  it('should return undefined date is null or undefined fro preprocessDateForPicker', () => {
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
})
