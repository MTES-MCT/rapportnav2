import { useEffect, useState } from 'react'

const useApolloLastSync = (): string | undefined => {
  const [lastSync, setLastSync] = useState<string | undefined>(() => {
    return localStorage.getItem('lastSyncTimestamp') ?? undefined
  })

  useEffect(() => {
    const handleStorageChange = (event: StorageEvent) => {
      if (event.key === 'lastSyncTimestamp') {
        setLastSync(event.newValue ?? undefined)
      }
    }
    window.addEventListener('storage', handleStorageChange)
  }, [])
  return lastSync
}

export default useApolloLastSync
