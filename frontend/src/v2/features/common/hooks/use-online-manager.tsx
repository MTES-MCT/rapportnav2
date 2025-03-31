import { useEffect, useState } from 'react'
import { onlineManager } from '@tanstack/react-query'

export type OnlineManagerHook = {
  isOnline: boolean
  isOffline: boolean
  setOnline: (online: boolean) => void
}

export function useOnlineManager(): OnlineManagerHook {
  const [isOnline, setIsOnline] = useState<boolean>(onlineManager.isOnline())

  useEffect(() => {
    const handleOnlineChange = (online: boolean) => {
      setIsOnline(online)
      setOnline(online)
    }

    const unsubscribe = onlineManager.subscribe(handleOnlineChange)

    return () => {
      unsubscribe()
    }
  }, [])

  const setOnline = (online: boolean) => {
    setIsOnline(online)
    onlineManager.setOnline(online)
  }

  return {
    isOnline,
    isOffline: !isOnline,
    setOnline
  }
}
