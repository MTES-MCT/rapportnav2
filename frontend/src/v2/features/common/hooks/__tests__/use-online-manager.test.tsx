import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { useOnlineManager } from '../use-online-manager.tsx'
import { onlineManager } from '@tanstack/react-query'
import { renderHook, act, waitFor } from '../../../../../test-utils.tsx'

describe('useOnlineManager', () => {
  let isOnline = true

  beforeEach(() => {
    // initial TanStack Query state
    vi.spyOn(onlineManager, 'isOnline').mockReturnValue(isOnline)
    // subscribe should immediately callback with current isOnline
    vi.spyOn(onlineManager, 'subscribe').mockImplementation(cb => {
      cb(isOnline)
      return () => {}
    })
    // track when we push state back into TanStack Query
    vi.spyOn(onlineManager, 'setOnline').mockImplementation(val => {
      isOnline = val
    })
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  it('initial state comes from onlineManager.isOnline()', () => {
    const { result } = renderHook(() => useOnlineManager())
    expect(result.current.isOnline).toBe(true)
    expect(result.current.isOffline).toBe(false)
    expect(result.current.hasNetwork).toBe(true)
  })

  it('subscribes to onlineManager on mount', () => {
    renderHook(() => useOnlineManager())
    expect(onlineManager.subscribe).toHaveBeenCalled()
  })

  it('toggleOffline() forces offline even if network is up', async () => {
    const { result } = renderHook(() => useOnlineManager())

    act(() => {
      result.current.toggleOnline(false)
    })

    await waitFor(() => {
      expect(result.current.isOnline).toBe(false)
      expect(result.current.isOffline).toBe(true)
      expect(result.current.hasNetwork).toBe(true)
      expect(onlineManager.setOnline).toHaveBeenCalledWith(false)
    })
  })

  it('toggleOnline(true) only re-enables when network is available', async () => {
    const { result } = renderHook(() => useOnlineManager())

    // 1) Simulate going offline via event
    act(() => {
      window.dispatchEvent(new Event('offline'))
    })
    // hasNetwork should now be false
    expect(result.current.hasNetwork).toBe(false)
    // forcing online while offline does nothing
    act(() => {
      result.current.toggleOnline(true)
    })
    expect(result.current.isOnline).toBe(false)

    // 2) Simulate network return
    act(() => {
      window.dispatchEvent(new Event('online'))
    })
    expect(result.current.hasNetwork).toBe(true)

    // 3) Now user toggles online again
    act(() => {
      result.current.toggleOnline(true)
    })

    await waitFor(() => {
      expect(result.current.isOnline).toBe(true)
      expect(onlineManager.setOnline).toHaveBeenCalledWith(true)
    })
  })
})
