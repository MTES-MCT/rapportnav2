import { useRef } from 'react'

const DEBOUNCE_TIME_TRIGGER = 3000

type DelayHook = {
  handleExecuteOnDelay: (callback: () => Promise<void> | void, debounceTime?: number) => Promise<void>
}

export function useDelay(): DelayHook {
  const timerRef = useRef<ReturnType<typeof setTimeout>>()

  const handleExecuteOnDelay = async (callback: () => Promise<void> | void, debounceTime?: number): Promise<void> => {
    clearTimeout(timerRef.current)
    timerRef.current = setTimeout(async () => {
      await callback()
    }, debounceTime ?? DEBOUNCE_TIME_TRIGGER)
  }

  return {
    handleExecuteOnDelay
  }
}
