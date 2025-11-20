import { setOfflineSince } from '../../../store/slices/connectivity-reducer.ts'
import { State, store } from '../../../store'
import { useStore } from '@tanstack/react-store'
import { differenceInSeconds, parseISO } from 'date-fns'
import { UTCDate } from '@date-fns/utc'
import { useCallback, useEffect } from 'react'

export const OFFLINE_EXPIRY_CHECK_DURATION = 86400 // 24h = 86400s
export const OFFLINE_SINCE_KEY = 'app_offline_since' // Export the key

// Initialize store from localStorage on module load
try {
  const storedOfflineSince = localStorage.getItem(OFFLINE_SINCE_KEY)
  if (storedOfflineSince) {
    setOfflineSince(storedOfflineSince)
  }
} catch (error) {
  console.error('Failed to read offline state from localStorage:', error)
}

export function useOfflineSince() {
  const offlineSince = useStore(store, (state: State) => state.connectivity.offlineSince)

  // Sync to localStorage whenever offlineSince changes
  useEffect(() => {
    try {
      if (offlineSince) {
        localStorage.setItem(OFFLINE_SINCE_KEY, offlineSince)
      } else {
        localStorage.removeItem(OFFLINE_SINCE_KEY)
      }
    } catch (error) {
      console.error('Failed to persist offline state to localStorage:', error)
    }
  }, [offlineSince])

  const setOfflineSinceFn = useCallback((isOnline: boolean) => {
    const newValue = isOnline ? undefined : new UTCDate().toISOString()
    setOfflineSince(newValue)
  }, [])

  const isOfflineSinceTooLong = useCallback(() => {
    if (!offlineSince) {
      return false
    }

    try {
      const secondsOffline = differenceInSeconds(new UTCDate(), parseISO(offlineSince))
      return secondsOffline >= OFFLINE_EXPIRY_CHECK_DURATION
    } catch (error) {
      // If date parsing fails, treat as not too long
      console.error('Error parsing offlineSince date:', error)
      return false
    }
  }, [offlineSince])

  return {
    offlineSince,
    setOfflineSince: setOfflineSinceFn,
    isOfflineSinceTooLong
  }
}
