import { useState, useEffect } from 'react'

const ENABLE_OFFLINE_MODE_KEY = 'ENABLE_OFFLINE_MODE'

// Check offline mode from localStorage
export const getOfflineMode = (): boolean => {
  return true
  // try {
  //   const stored = localStorage.getItem(ENABLE_OFFLINE_MODE_KEY)
  //   return stored === 'true'
  // } catch (error) {
  //   return false
  // }
}

// hook to enable the offline mode by checking the ENABLE_OFFLINE_MODE local storage key
export function useOfflineMode() {
  const [isOfflineMode, setIsOfflineMode] = useState<boolean>(() => getOfflineMode())

  useEffect(() => {
    // Listen for localStorage changes from other tabs/windows
    const handleStorageChange = (e: any) => {
      if (e.key === ENABLE_OFFLINE_MODE_KEY) {
        setIsOfflineMode(e.newValue === 'true')
      }
    }

    window.addEventListener('storage', handleStorageChange)

    return () => {
      window.removeEventListener('storage', handleStorageChange)
    }
  }, [])

  return isOfflineMode
}
