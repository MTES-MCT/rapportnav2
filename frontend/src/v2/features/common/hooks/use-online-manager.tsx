import { useEffect, useState, useCallback } from 'react'
import { onlineManager } from '@tanstack/react-query'

/**
 * Offline-first toggle hook.
 * - Detects physical network availability via browser events.
 * - Allows users to manually switch to offline mode even when network is available.
 * - Integrates with TanStack Query's onlineManager for query behavior.
 */
export function useOnlineManager() {
  // 1) Physical network availability
  const [hasNetwork, setHasNetwork] = useState(() => navigator.onLine)

  // 2) User-driven offline mode ("manual offline")
  const [manualOffline, setManualOffline] = useState(false)

  // 3) Effective online state from TanStack Query
  const [isOnline, setIsOnline] = useState(() => onlineManager.isOnline())

  // Subscribe to TanStack Query onlineManager changes
  useEffect(() => {
    const handleChange = online => setIsOnline(online)
    const unsubscribe = onlineManager.subscribe(handleChange)
    return unsubscribe
  }, [])

  // Sync TanStack Query & internal state whenever network or manualOffline changes
  useEffect(() => {
    const effective = hasNetwork && !manualOffline
    onlineManager.setOnline(effective)
    setIsOnline(effective)
  }, [hasNetwork, manualOffline])

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
    shouldBeOnline => {
      if (shouldBeOnline) {
        // Only clear manualOffline if there's physical network
        if (hasNetwork) {
          setManualOffline(false)
        }
      } else {
        // User forces offline
        setManualOffline(true)
      }
    },
    [hasNetwork]
  )

  return {
    hasNetwork, // true if browser reports network available
    manualOffline, // true if user has chosen offline mode
    isOnline, // effective online state (TanStack Query)
    isOffline: !isOnline, // effective offline state
    toggleOnline // call with `true|false` to switch modes
  }
}
