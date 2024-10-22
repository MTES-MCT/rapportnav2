import { useRef } from 'react'

const DEBOUNCE_TIME_TRIGGER = 5000

type DelayHook = {
  handleExecuteOnDelay: (callback: () => Promise<void> | void) => Promise<void>
}

export function useDelay(): DelayHook {
  const timerRef = useRef<ReturnType<typeof setTimeout>>()

  const handleExecuteOnDelay = async (callback: () => void, debounceTime?: number): Promise<void> => {
    clearTimeout(timerRef.current)
    timerRef.current = setTimeout(() => callback(), debounceTime || DEBOUNCE_TIME_TRIGGER)
  }

  return {
    handleExecuteOnDelay
  }
}
