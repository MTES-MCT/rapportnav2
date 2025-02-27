import { renderHook } from '@testing-library/react'
import { vi } from 'vitest'
import { useDelay } from '../use-delay'

describe('useDelay', () => {
  it('should execute with default debounce time delay', () => {
    const handleExecute = vi.fn()
    vi.useFakeTimers({ shouldAdvanceTime: true })
    const { result } = renderHook(() => useDelay())
    result.current.handleExecuteOnDelay(handleExecute)
    expect(handleExecute).not.toHaveBeenCalled()
    vi.advanceTimersByTime(5000)
    expect(handleExecute).toHaveBeenCalledTimes(1)
  })
  it('should execute with debounce time delay', () => {
    const debounceTime = 8000
    const handleExecute = vi.fn()
    vi.useFakeTimers({ shouldAdvanceTime: true })
    const { result } = renderHook(() => useDelay())
    result.current.handleExecuteOnDelay(handleExecute, debounceTime)
    expect(handleExecute).not.toHaveBeenCalled()
    vi.advanceTimersByTime(debounceTime)
    expect(handleExecute).toHaveBeenCalledTimes(1)
  })
})
