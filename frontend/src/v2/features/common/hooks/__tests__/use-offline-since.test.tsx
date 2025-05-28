import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { renderHook, act } from '@testing-library/react'
import { OFFLINE_EXPIRY_CHECK_DURATION, useOfflineSince } from '../use-offline-since.tsx'
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
  beforeEach(() => {
    vi.clearAllMocks()
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
      expect(mockDifferenceInSeconds).toHaveBeenCalledWith(
        new Date('2024-01-01T12:00:00Z'), // current time
        parsedDate
      )
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
  })

  describe('integration tests', () => {
    it('should handle complete offline/online cycle', () => {
      // Start online
      mockUseStore.mockReturnValue(undefined)

      const { result, rerender } = renderHook(() => useOfflineSince())

      expect(result.current.offlineSince).toBeUndefined()
      expect(result.current.isOfflineSinceTooLong()).toBe(false)

      // Go offline
      act(() => {
        result.current.setOfflineSince(false)
      })

      expect(mockSetOfflineSince).toHaveBeenCalledWith('2024-01-01T12:00:00.000Z')

      // Simulate being offline for a while
      mockUseStore.mockReturnValue('2024-01-01T11:50:00Z')
      const parsedDate = new Date('2024-01-01T11:50:00Z')
      mockParseISO.mockReturnValue(parsedDate)
      mockDifferenceInSeconds.mockReturnValue(600) // 10 minutes

      rerender()

      expect(result.current.offlineSince).toBe('2024-01-01T11:50:00Z')
      expect(result.current.isOfflineSinceTooLong()).toBe(true)

      // Go back online
      act(() => {
        result.current.setOfflineSince(true)
      })

      expect(mockSetOfflineSince).toHaveBeenCalledWith(undefined)
    })
  })

  describe('edge cases', () => {
    it('should handle invalid date strings gracefully', () => {
      const invalidTimestamp = 'invalid-date'
      mockUseStore.mockReturnValue(invalidTimestamp)
      mockParseISO.mockImplementation(() => {
        throw new Error('Invalid date')
      })

      const { result } = renderHook(() => useOfflineSince())

      expect(() => result.current.isOfflineSinceTooLong()).toThrow('Invalid date')
      expect(mockParseISO).toHaveBeenCalledWith(invalidTimestamp)
    })

    it('should handle negative time differences', () => {
      const offlineTimestamp = '2024-01-01T13:00:00Z' // future time
      mockUseStore.mockReturnValue(offlineTimestamp)

      const parsedDate = new Date('2024-01-01T13:00:00Z')
      mockParseISO.mockReturnValue(parsedDate)
      mockDifferenceInSeconds.mockReturnValue(-3600) // negative difference

      const { result } = renderHook(() => useOfflineSince())

      expect(result.current.isOfflineSinceTooLong()).toBe(false)
    })
  })
})
