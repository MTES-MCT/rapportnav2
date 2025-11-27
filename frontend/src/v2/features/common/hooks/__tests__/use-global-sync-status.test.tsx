import { act, renderHook } from '@testing-library/react'
import { useGlobalSyncStatus } from '../use-global-sync-status.tsx'
import { useIsMutating, useIsFetching } from '@tanstack/react-query'
import { vi, describe, it, expect, beforeEach, afterEach } from 'vitest'

vi.mock('@tanstack/react-query', () => ({
  useIsMutating: vi.fn(),
  useIsFetching: vi.fn()
}))

describe('useGlobalSyncStatus', () => {
  beforeEach(() => {
    vi.useFakeTimers()
    vi.clearAllMocks()
  })

  afterEach(() => {
    vi.useRealTimers()
  })

  function mockQueryState({ mutating = 0, fetching = 0 }) {
    ;(useIsMutating as unknown as vi.Mock).mockReturnValue(mutating)
    ;(useIsFetching as unknown as vi.Mock).mockReturnValue(fetching)
  }

  it('initially inactive when nothing is mutating or fetching', () => {
    mockQueryState({ mutating: 0, fetching: 0 })

    const { result } = renderHook(() => useGlobalSyncStatus())
    expect(result.current.active).toBe(false)
    expect(result.current.mutatingCount).toBe(0)
  })

  it('activates immediately when both mutating and fetching start', () => {
    mockQueryState({ mutating: 1, fetching: 1 })

    const { result, rerender } = renderHook(() => useGlobalSyncStatus())

    expect(result.current.active).toBe(true)

    // sanity check: stays active with higher values
    mockQueryState({ mutating: 4, fetching: 2 })
    rerender()

    expect(result.current.active).toBe(true)
  })

  it('does NOT activate when only mutating OR only fetching', () => {
    mockQueryState({ mutating: 1, fetching: 0 })
    const { result } = renderHook(() => useGlobalSyncStatus())
    expect(result.current.active).toBe(false)

    mockQueryState({ mutating: 0, fetching: 3 })
    const { result: result2 } = renderHook(() => useGlobalSyncStatus())
    expect(result2.current.active).toBe(false)
  })

  it('deactivates only after the stable delay when idle', () => {
    // Start busy
    mockQueryState({ mutating: 2, fetching: 1 })
    const { result, rerender } = renderHook(() => useGlobalSyncStatus(300))

    expect(result.current.active).toBe(true)

    // Now idle
    mockQueryState({ mutating: 0, fetching: 0 })
    rerender()

    // Still active immediately
    expect(result.current.active).toBe(true)

    // Wait past timer
    act(() => {
      vi.advanceTimersByTime(300)
    })

    expect(result.current.active).toBe(false)
  })

  it('cancels hide delay if activity resumes before timer completes', () => {
    // Start busy
    mockQueryState({ mutating: 1, fetching: 1 })
    const { result, rerender } = renderHook(() => useGlobalSyncStatus(300))

    expect(result.current.active).toBe(true)

    // Now idle -> hide scheduled
    mockQueryState({ mutating: 0, fetching: 0 })
    rerender()

    // Before hide timeout, become busy again
    mockQueryState({ mutating: 1, fetching: 1 })
    rerender()

    act(() => {
      vi.advanceTimersByTime(300)
    })

    // Should remain active because hide was cancelled
    expect(result.current.active).toBe(true)
  })
})
