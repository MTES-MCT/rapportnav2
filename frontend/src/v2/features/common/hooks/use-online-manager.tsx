import { useEffect, useState, useCallback } from 'react'
import { onlineManager } from '@tanstack/react-query'
import { useOfflineSince, OFFLINE_SINCE_KEY } from './use-offline-since.tsx'

/**
 * Offline-first toggle hook.
 * - Detects physical network availability via browser events.
 * - Allows users to manually switch to offline mode even when network is available.
 * - Integrates with TanStack Query's onlineManager for query behavior.
 */
export function useOnlineManager() {
  const { offlineSince, setOfflineSince } = useOfflineSince()

  // Physical network availability
  const [hasNetwork, setHasNetwork] = useState(() => navigator.onLine)

  // Track if user manually went offline (separate from network state)
  const [manualOffline, setManualOffline] = useState(() => {
    const storedOfflineSince = localStorage.getItem(OFFLINE_SINCE_KEY)
    return !!storedOfflineSince
  })

  // Effective online state - check localStorage directly for initial value
  const [isOnline, setIsOnline] = useState(() => {
    const storedOfflineSince = localStorage.getItem(OFFLINE_SINCE_KEY)
    const wasManuallyOffline = !!storedOfflineSince
    const initialOnline = navigator.onLine && !wasManuallyOffline

    // Set onlineManager immediately
    onlineManager.setOnline(initialOnline)

    return initialOnline
  })

  // Stable compute function
  const computeStatus = useCallback(
    (hasNetwork: boolean, isManualOffline: boolean) => {
      const effective = hasNetwork && !isManualOffline
      if (effective !== onlineManager.isOnline()) {
        onlineManager.setOnline(effective)
        setIsOnline(effective)
        // Only set offlineSince if manually offline
        if (isManualOffline) {
          setOfflineSince(false) // going offline
        } else if (hasNetwork) {
          setOfflineSince(true) // going online (only if network available)
        }
      }
    },
    [setOfflineSince]
  )

  // Subscribe to TanStack Query onlineManager changes
  useEffect(() => {
    const handleChange = (online: boolean) => setIsOnline(online)
    const unsubscribe = onlineManager.subscribe(handleChange)
    return unsubscribe
  }, [])

  // Sync state when network or manual offline status changes
  useEffect(() => {
    computeStatus(hasNetwork, manualOffline)
  }, [hasNetwork, manualOffline, computeStatus])

  // Listen to browser connectivity changes
  useEffect(() => {
    const onOnline = () => setHasNetwork(true)
    const onOffline = () => setHasNetwork(false)

    window.addEventListener('online', onOnline)
    window.addEventListener('offline', onOffline)

    return () => {
      window.removeEventListener('online', onOnline)
      window.removeEventListener('offline', onOffline)
    }
  }, [])

  // User toggle function
  const toggleOnline = useCallback(
    (shouldBeOnline: boolean) => {
      if (shouldBeOnline) {
        // Clear manual offline state
        setManualOffline(false)
        if (hasNetwork) {
          computeStatus(hasNetwork, false)
        }
      } else {
        // User forces offline
        setManualOffline(true)
        computeStatus(hasNetwork, true)
      }
    },
    [hasNetwork, computeStatus]
  )

  return {
    hasNetwork, // true if browser reports network available
    isOnline, // effective online state (TanStack Query)
    isOffline: !isOnline, // effective offline state
    toggleOnline // call with `true|false` to switch modes
  }
}
