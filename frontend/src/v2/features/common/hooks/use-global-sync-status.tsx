import { useEffect, useRef, useState } from 'react'
import { useIsMutating, useIsFetching } from '@tanstack/react-query'

export function useGlobalSyncStatus(stableDelay = 300) {
  const mutatingCount = useIsMutating()
  const fetchingCount = useIsFetching()

  // true if both fetching AND mutating are in progress
  const [active, setActive] = useState(mutatingCount > 0 && fetchingCount > 0)
  const timerRef = useRef<number | null>(null)

  useEffect(() => {
    const busy = mutatingCount > 0 && fetchingCount > 0

    if (busy) {
      // immediately show loader
      setActive(true)

      // cancel any scheduled hide
      if (timerRef.current) {
        clearTimeout(timerRef.current)
        timerRef.current = null
      }
    } else {
      // schedule hiding the loader after delay
      if (!timerRef.current) {
        timerRef.current = window.setTimeout(() => {
          // only hide if still idle
          if (mutatingCount === 0 || fetchingCount === 0) {
            setActive(false)
          }
          timerRef.current = null
        }, stableDelay)
      }
    }

    return () => {
      if (timerRef.current) {
        clearTimeout(timerRef.current)
        timerRef.current = null
      }
    }
  }, [mutatingCount, fetchingCount, stableDelay])

  return { active, mutatingCount }
}
