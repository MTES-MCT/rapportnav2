import { useEffect, useState } from 'react'
import { onlineManager } from '@tanstack/react-query'
import { useOfflineSince } from './use-offline-since.tsx'

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

  // Effective online state from TanStack Query
  const [isOnline, setIsOnline] = useState(() => onlineManager.isOnline())

  // Subscribe to TanStack Query onlineManager changes
  useEffect(() => {
    const handleChange = online => setIsOnline(online)
    const unsubscribe = onlineManager.subscribe(handleChange)
    return unsubscribe
  }, [])

  const computeStatus = (hasNetwork: boolean, manualOffline: boolean) => {
    const effective = hasNetwork && !manualOffline
    if (effective !== onlineManager.isOnline()) {
      onlineManager.setOnline(effective)
      setIsOnline(effective)

      setOfflineSince(effective)
    }
  }

  // Sync TanStack Query & internal state whenever network or manualOffline changes
  useEffect(() => {
    computeStatus(hasNetwork, !!offlineSince)
  }, [computeStatus, hasNetwork, offlineSince, setOfflineSince])

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
  const toggleOnline = (shouldBeOnline: boolean) => {
    if (shouldBeOnline) {
      // Only clear manualOffline if there's physical network
      if (hasNetwork) {
        computeStatus(hasNetwork, false)
      }
    } else {
      // User forces offline
      computeStatus(hasNetwork, true)
    }
  }

  return {
    hasNetwork, // true if browser reports network available
    isOnline, // effective online state (TanStack Query)
    isOffline: !isOnline, // effective offline state
    toggleOnline // call with `true|false` to switch modes
  }
}
