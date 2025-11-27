import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { renderHook, act } from '@testing-library/react'
import { OFFLINE_EXPIRY_CHECK_DURATION, OFFLINE_SINCE_KEY, useOfflineSince } from '../use-offline-since.tsx'
import * as connectivityReducer from '../../../../store/slices/connectivity-reducer'
import { useStore } from '@tanstack/react-store'
import { differenceInSeconds, parseISO } from 'date-fns'

// Mock the store and reducer
vi.mock('../../../../store/slices/connectivity-reducer', () => ({
  setOfflineSince: vi.fn()
}))

vi.mock('../../../store', () => ({
  store: {
    setState: vi.fn(),
    getState: vi.fn(() => ({
      connectivity: {
        offlineSince: undefined
      }
    }))
  }
}))

vi.mock('@tanstack/react-store', () => ({
  useStore: vi.fn()
}))

// Mock date-fns functions
vi.mock('date-fns', () => ({
  differenceInSeconds: vi.fn(),
  parseISO: vi.fn()
}))

const mockUseStore = vi.mocked(useStore)
const mockDifferenceInSeconds = vi.mocked(differenceInSeconds)
const mockParseISO = vi.mocked(parseISO)
const mockSetOfflineSince = vi.mocked(connectivityReducer.setOfflineSince)

describe('useOfflineSince', () => {
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
    vi.clearAllMocks()
    localStorageMock.clear()
    Object.defineProperty(window, 'localStorage', {
      value: localStorageMock,
      writable: true
    })
    // Reset Date mock
    vi.setSystemTime(new Date('2024-01-01T12:00:00Z'))
  })

  afterEach(() => {
    vi.useRealTimers()
  })

  describe('offlineSince', () => {
    it('should return undefined when not offline', () => {
      mockUseStore.mockReturnValue(undefined)

      const { result } = renderHook(() => useOfflineSince())

      expect(result.current.offlineSince).toBeUndefined()
    })

    it('should return offline timestamp when offline', () => {
      const offlineTimestamp = '2024-01-01T11:00:00Z'
      mockUseStore.mockReturnValue(offlineTimestamp)

      const { result } = renderHook(() => useOfflineSince())

      expect(result.current.offlineSince).toBe(offlineTimestamp)
    })
  })

  describe('setOfflineSince', () => {
    it('should set offlineSince to undefined when going online', () => {
      mockUseStore.mockReturnValue('2024-01-01T11:00:00Z')

      const { result } = renderHook(() => useOfflineSince())

      act(() => {
        result.current.setOfflineSince(true) // isOnline = true
      })

      expect(mockSetOfflineSince).toHaveBeenCalledWith(undefined)
    })

    it('should set offlineSince to current timestamp when going offline', () => {
      mockUseStore.mockReturnValue(undefined)

      const { result } = renderHook(() => useOfflineSince())

      act(() => {
        result.current.setOfflineSince(false) // isOnline = false
      })

      expect(mockSetOfflineSince).toHaveBeenCalledWith('2024-01-01T12:00:00.000Z')
    })
  })

  describe('localStorage persistence', () => {
    it('should save to localStorage when offlineSince is set', () => {
      const offlineTimestamp = '2024-01-01T11:00:00Z'
      mockUseStore.mockReturnValue(undefined)

      const { rerender } = renderHook(() => useOfflineSince())

      // Simulate store update
      mockUseStore.mockReturnValue(offlineTimestamp)
      rerender()

      expect(localStorageMock.setItem).toHaveBeenCalledWith(OFFLINE_SINCE_KEY, offlineTimestamp)
    })

    it('should remove from localStorage when going back online', () => {
      const offlineTimestamp = '2024-01-01T11:00:00Z'
      mockUseStore.mockReturnValue(offlineTimestamp)

      const { rerender } = renderHook(() => useOfflineSince())

      // Simulate going back online
      mockUseStore.mockReturnValue(undefined)
      rerender()

      expect(localStorageMock.removeItem).toHaveBeenCalledWith(OFFLINE_SINCE_KEY)
    })

    it('should not save to localStorage when offlineSince is already undefined', () => {
      mockUseStore.mockReturnValue(undefined)

      renderHook(() => useOfflineSince())

      expect(localStorageMock.removeItem).toHaveBeenCalledWith(OFFLINE_SINCE_KEY)
    })
  })

  describe('isOfflineSinceTooLong', () => {
    it('should return false when not offline', () => {
      mockUseStore.mockReturnValue(undefined)

      const { result } = renderHook(() => useOfflineSince())

      expect(result.current.isOfflineSinceTooLong()).toBe(false)
    })

    it('should return false when offline for less than expiry duration', () => {
      const offlineTimestamp = '2024-01-01T11:00:00Z'
      mockUseStore.mockReturnValue(offlineTimestamp)

      const parsedDate = new Date(offlineTimestamp)
      mockParseISO.mockReturnValue(parsedDate)
      mockDifferenceInSeconds.mockReturnValue(1)

      const { result } = renderHook(() => useOfflineSince())

      expect(result.current.isOfflineSinceTooLong()).toBe(false)
      expect(mockParseISO).toHaveBeenCalledWith(offlineTimestamp)
      expect(mockDifferenceInSeconds).toHaveBeenCalled()
    })

    it('should return true when offline for exactly the expiry duration', () => {
      const offlineTimestamp = '2024-01-01T11:00:00Z'
      mockUseStore.mockReturnValue(offlineTimestamp)

      const parsedDate = new Date('2024-01-01T11:00:00Z')
      mockParseISO.mockReturnValue(parsedDate)
      mockDifferenceInSeconds.mockReturnValue(OFFLINE_EXPIRY_CHECK_DURATION)

      const { result } = renderHook(() => useOfflineSince())

      expect(result.current.isOfflineSinceTooLong()).toBe(true)
    })

    it('should return true when offline for more than expiry duration', () => {
      const offlineTimestamp = '2024-01-01T11:00:00Z'
      mockUseStore.mockReturnValue(offlineTimestamp)

      const parsedDate = new Date('2024-01-01T11:00:00Z')
      mockParseISO.mockReturnValue(parsedDate)
      mockDifferenceInSeconds.mockReturnValue(OFFLINE_EXPIRY_CHECK_DURATION + 1000)

      const { result } = renderHook(() => useOfflineSince())

      expect(result.current.isOfflineSinceTooLong()).toBe(true)
    })

    it('should return false and log error when date parsing fails', () => {
      const consoleSpy = vi.spyOn(console, 'error').mockImplementation(() => {})
      const invalidTimestamp = 'invalid-date'
      mockUseStore.mockReturnValue(invalidTimestamp)
      mockParseISO.mockImplementation(() => {
        throw new Error('Invalid date')
      })

      const { result } = renderHook(() => useOfflineSince())

      expect(result.current.isOfflineSinceTooLong()).toBe(false)
      expect(mockParseISO).toHaveBeenCalledWith(invalidTimestamp)
      expect(consoleSpy).toHaveBeenCalledWith('Error parsing offlineSince date:', expect.any(Error))

      consoleSpy.mockRestore()
    })
  })

  describe('integration tests', () => {
    it('should handle complete offline/online cycle with localStorage', () => {
      // Start online
      mockUseStore.mockReturnValue(undefined)

      const { result, rerender } = renderHook(() => useOfflineSince())

      expect(result.current.offlineSince).toBeUndefined()
      expect(result.current.isOfflineSinceTooLong()).toBe(false)
      expect(localStorageMock.removeItem).toHaveBeenCalledWith(OFFLINE_SINCE_KEY)

      // Go offline
      act(() => {
        result.current.setOfflineSince(false)
      })

      expect(mockSetOfflineSince).toHaveBeenCalledWith('2024-01-01T12:00:00.000Z')

      // Simulate store update and localStorage sync
      const offlineTimestamp = '2024-01-01T12:00:00.000Z'
      mockUseStore.mockReturnValue(offlineTimestamp)
      rerender()

      expect(localStorageMock.setItem).toHaveBeenCalledWith(OFFLINE_SINCE_KEY, offlineTimestamp)

      // Simulate being offline for a while
      const oldTimestamp = '2023-01-01T11:50:00Z'
      mockUseStore.mockReturnValue(oldTimestamp)
      const parsedDate = new Date(oldTimestamp)
      mockParseISO.mockReturnValue(parsedDate)
      mockDifferenceInSeconds.mockReturnValue(86401) // 1 day + 1 sec

      rerender()

      expect(result.current.offlineSince).toBe(oldTimestamp)
      expect(result.current.isOfflineSinceTooLong()).toBe(true)

      // Go back online
      act(() => {
        result.current.setOfflineSince(true)
      })

      expect(mockSetOfflineSince).toHaveBeenCalledWith(undefined)

      // Simulate store update - back online
      mockUseStore.mockReturnValue(undefined)
      rerender()

      expect(localStorageMock.removeItem).toHaveBeenCalledWith(OFFLINE_SINCE_KEY)
    })
  })

  describe('edge cases', () => {
    it('should handle negative time differences', () => {
      const offlineTimestamp = '2024-01-01T13:00:00Z' // future time
      mockUseStore.mockReturnValue(offlineTimestamp)

      const parsedDate = new Date('2024-01-01T13:00:00Z')
      mockParseISO.mockReturnValue(parsedDate)
      mockDifferenceInSeconds.mockReturnValue(-3600) // negative difference

      const { result } = renderHook(() => useOfflineSince())

      expect(result.current.isOfflineSinceTooLong()).toBe(false)
    })

    it('should handle localStorage access errors gracefully', () => {
      const offlineTimestamp = '2024-01-01T11:00:00Z'
      mockUseStore.mockReturnValue(offlineTimestamp)

      // Simulate localStorage.setItem throwing an error
      localStorageMock.setItem.mockImplementation(() => {
        throw new Error('QuotaExceededError')
      })

      // Should not crash
      expect(() => {
        const { rerender } = renderHook(() => useOfflineSince())
        rerender()
      }).not.toThrow()
    })
  })
})
