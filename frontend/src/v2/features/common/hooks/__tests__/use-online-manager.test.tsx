import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { useOnlineManager } from '../use-online-manager.tsx'
import { onlineManager } from '@tanstack/react-query'
import { renderHook, act, waitFor } from '../../../../../test-utils.tsx'
import { OFFLINE_SINCE_KEY } from '../use-offline-since.tsx'

describe('useOnlineManager', () => {
  let isOnline = true

  // Mock localStorage
  const localStorageMock = (() => {
    let store: Record<string, string> = {}
    return {
      getItem: vi.fn((key: string) => store[key] || null),
      setItem: vi.fn((key: string, value: string) => {
        store[key] = value
      }),
      removeItem: vi.fn((key: string) => {
        delete store[key]
      }),
      clear: vi.fn(() => {
        store = {}
      })
    }
  })()

  beforeEach(() => {
    // Reset localStorage
    localStorageMock.clear()
    Object.defineProperty(window, 'localStorage', {
      value: localStorageMock,
      writable: true,
      configurable: true
    })

    // Reset online state
    isOnline = true

    // Mock navigator.onLine
    Object.defineProperty(navigator, 'onLine', {
      writable: true,
      value: true,
      configurable: true
    })

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

  it('initial state comes from onlineManager when no persisted offline state', () => {
    const { result } = renderHook(() => useOnlineManager())
    expect(result.current.isOnline).toBe(true)
    expect(result.current.isOffline).toBe(false)
    expect(result.current.hasNetwork).toBe(true)
  })

  it('initializes as offline when localStorage has offlineSince', () => {
    // Simulate persisted offline state
    localStorageMock.getItem.mockReturnValue('2024-01-01T11:00:00Z')

    const { result } = renderHook(() => useOnlineManager())

    expect(result.current.isOnline).toBe(false)
    expect(result.current.isOffline).toBe(true)
    expect(result.current.hasNetwork).toBe(true)
    expect(onlineManager.setOnline).toHaveBeenCalledWith(false)
  })

  it('initializes as online when network is down, even with persisted offline state', () => {
    // Simulate persisted offline state
    localStorageMock.getItem.mockReturnValue('2024-01-01T11:00:00Z')
    // But network is down
    Object.defineProperty(navigator, 'onLine', {
      writable: true,
      value: false,
      configurable: true
    })

    const { result } = renderHook(() => useOnlineManager())

    // Should be offline because network is down
    expect(result.current.isOnline).toBe(false)
    expect(result.current.isOffline).toBe(true)
    expect(result.current.hasNetwork).toBe(false)
  })

  it('subscribes to onlineManager on mount', () => {
    renderHook(() => useOnlineManager())
    expect(onlineManager.subscribe).toHaveBeenCalled()
  })

  it('toggleOnline(false) forces offline even if network is up', async () => {
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
      Object.defineProperty(navigator, 'onLine', {
        writable: true,
        value: false,
        configurable: true
      })
      window.dispatchEvent(new Event('offline'))
    })

    await waitFor(() => {
      expect(result.current.hasNetwork).toBe(false)
    })

    // forcing online while network is offline does nothing
    act(() => {
      result.current.toggleOnline(true)
    })
    expect(result.current.isOnline).toBe(false)

    // 2) Simulate network return
    act(() => {
      Object.defineProperty(navigator, 'onLine', {
        writable: true,
        value: true,
        configurable: true
      })
      window.dispatchEvent(new Event('online'))
    })

    await waitFor(() => {
      expect(result.current.hasNetwork).toBe(true)
    })

    // 3) Now user toggles online again
    act(() => {
      result.current.toggleOnline(true)
    })

    await waitFor(() => {
      expect(onlineManager.setOnline).toHaveBeenCalledWith(true)
    })
  })

  it('persists offline state to localStorage when toggled offline', async () => {
    const { result } = renderHook(() => useOnlineManager())

    act(() => {
      result.current.toggleOnline(false)
    })

    await waitFor(() => {
      expect(localStorageMock.setItem).toHaveBeenCalledWith(OFFLINE_SINCE_KEY, expect.any(String))
    })
  })

  it('restores offline state after page reload', () => {
    // First session: go offline
    localStorageMock.getItem.mockReturnValue(null)
    const { result: result1, unmount } = renderHook(() => useOnlineManager())

    act(() => {
      result1.current.toggleOnline(false)
    })

    expect(result1.current.isOffline).toBe(true)

    // Simulate page reload
    unmount()

    // Second session: should restore offline state
    localStorageMock.getItem.mockReturnValue('2024-01-01T11:00:00Z')
    const { result: result2 } = renderHook(() => useOnlineManager())

    expect(result2.current.isOffline).toBe(true)
    expect(result2.current.isOnline).toBe(false)
  })

  it('prioritizes manual offline over network availability', async () => {
    const { result } = renderHook(() => useOnlineManager())

    // User manually goes offline
    act(() => {
      result.current.toggleOnline(false)
    })

    await waitFor(() => {
      expect(result.current.isOffline).toBe(true)
    })

    // Even if browser reports online, manual offline should persist
    act(() => {
      window.dispatchEvent(new Event('online'))
    })

    // Should still be offline due to manual toggle
    expect(result.current.isOffline).toBe(true)
    expect(result.current.hasNetwork).toBe(true)
  })
})
