import { describe, it, expect, beforeEach, afterEach, vi } from 'vitest'
import useOfflineMode from '../use-offline-mode.tsx'
import { renderHook, act } from '../../../../../test-utils.tsx'

describe('useOfflineMode', () => {
  beforeEach(() => {
    // Clear localStorage before each test
    localStorage.clear()

    // Mock localStorage if needed
    Object.defineProperty(window, 'localStorage', {
      value: {
        getItem: vi.fn(),
        setItem: vi.fn(),
        removeItem: vi.fn(),
        clear: vi.fn()
      },
      writable: true
    })
  })

  afterEach(() => {
    vi.clearAllMocks()
  })

  it('should return false when localStorage key does not exist', () => {
    localStorage.getItem = vi.fn().mockReturnValue(null)

    const { result } = renderHook(() => useOfflineMode())

    expect(result.current).toBe(false)
    expect(localStorage.getItem).toHaveBeenCalledWith('ENABLE_OFFLINE_MODE')
  })

  it('should return true when localStorage key is "true"', () => {
    localStorage.getItem = vi.fn().mockReturnValue('true')

    const { result } = renderHook(() => useOfflineMode())

    expect(result.current).toBe(true)
    expect(localStorage.getItem).toHaveBeenCalledWith('ENABLE_OFFLINE_MODE')
  })

  it('should return false when localStorage key is "false"', () => {
    localStorage.getItem = vi.fn().mockReturnValue('false')

    const { result } = renderHook(() => useOfflineMode())

    expect(result.current).toBe(false)
  })

  it('should return false when localStorage key has any other value', () => {
    localStorage.getItem = vi.fn().mockReturnValue('something else')

    const { result } = renderHook(() => useOfflineMode())

    expect(result.current).toBe(false)
  })

  it('should return false when localStorage throws an error', () => {
    localStorage.getItem = vi.fn().mockImplementation(() => {
      throw new Error('localStorage not available')
    })

    const { result } = renderHook(() => useOfflineMode())

    expect(result.current).toBe(false)
  })

  it('should update when storage event is fired with matching key', () => {
    localStorage.getItem = vi.fn().mockReturnValue('false')

    const { result } = renderHook(() => useOfflineMode())

    expect(result.current).toBe(false)

    // Simulate storage event
    act(() => {
      const storageEvent = new StorageEvent('storage', {
        key: 'ENABLE_OFFLINE_MODE',
        newValue: 'true'
      })
      window.dispatchEvent(storageEvent)
    })

    expect(result.current).toBe(true)
  })

  it('should update to false when storage event has newValue as "false"', () => {
    localStorage.getItem = vi.fn().mockReturnValue('true')

    const { result } = renderHook(() => useOfflineMode())

    expect(result.current).toBe(true)

    // Simulate storage event
    act(() => {
      const storageEvent = new StorageEvent('storage', {
        key: 'ENABLE_OFFLINE_MODE',
        newValue: 'false'
      })
      window.dispatchEvent(storageEvent)
    })

    expect(result.current).toBe(false)
  })

  it('should not update when storage event is fired with different key', () => {
    localStorage.getItem = vi.fn().mockReturnValue('false')

    const { result } = renderHook(() => useOfflineMode())

    expect(result.current).toBe(false)

    // Simulate storage event with different key
    act(() => {
      const storageEvent = new StorageEvent('storage', {
        key: 'DIFFERENT_KEY',
        newValue: 'true'
      })
      window.dispatchEvent(storageEvent)
    })

    expect(result.current).toBe(false)
  })

  it('should clean up event listener on unmount', () => {
    const removeEventListenerSpy = vi.spyOn(window, 'removeEventListener')

    const { unmount } = renderHook(() => useOfflineMode())

    unmount()

    expect(removeEventListenerSpy).toHaveBeenCalledWith('storage', expect.any(Function))
  })

  it('should handle null newValue in storage event', () => {
    localStorage.getItem = vi.fn().mockReturnValue('true')

    const { result } = renderHook(() => useOfflineMode())

    expect(result.current).toBe(true)

    // Simulate storage event with null newValue (key deleted)
    act(() => {
      const storageEvent = new StorageEvent('storage', {
        key: 'ENABLE_OFFLINE_MODE',
        newValue: null
      })
      window.dispatchEvent(storageEvent)
    })

    expect(result.current).toBe(false)
  })
})
