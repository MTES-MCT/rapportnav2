import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { useOnlineManager } from '../use-online-manager.tsx'
import { onlineManager } from '@tanstack/react-query'
import { renderHook, waitFor } from '../../../../../test-utils.tsx'

describe('useOnlineManager', () => {
  let isOnline = true // Default state

  beforeEach(() => {
    vi.spyOn(onlineManager, 'isOnline').mockReturnValue(isOnline)
    vi.spyOn(onlineManager, 'subscribe').mockImplementation(cb => {
      console.log('Mocked subscribe called with:', isOnline)
      cb(isOnline) // Initial callback
      return () => {} // Mock unsubscribe
    })

    vi.spyOn(onlineManager, 'setOnline').mockImplementation(value => {
      console.log(`setOnline called with ${value}`)
      isOnline = value
    })
  })

  afterEach(() => {
    vi.restoreAllMocks()
  })

  it('initially sets isOnline state from onlineManager', () => {
    const { result } = renderHook(() => useOnlineManager())
    expect(result.current.isOnline).toBe(true)
    expect(result.current.isOffline).toBe(false)
  })

  it('calls subscribe on mount', () => {
    renderHook(() => useOnlineManager())
    expect(onlineManager.subscribe).toHaveBeenCalled()
  })

  it('updates state when setOnline is called', async () => {
    const { result } = renderHook(() => useOnlineManager())

    result.current.setOnline(false)
    await waitFor(() => {
      expect(result.current.isOnline).toBe(false)
      expect(result.current.isOffline).toBe(true)
      expect(onlineManager.setOnline).toHaveBeenCalledWith(false)
    })
  })
})
