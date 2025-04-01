import { setOfflineSince } from '../../../store/slices/connectivity-reducer.ts'
import { State, store } from '../../../store'
import { useStore } from '@tanstack/react-store'
import { differenceInSeconds, parseISO } from 'date-fns'
import { UTCDate } from '@date-fns/utc'

export const OFFLINE_EXPIRY_CHECK_DURATION = 86400 // 24h = 86400s
// export const OFFLINE_EXPIRY_CHECK_DURATION = 180 // 3 min

export function useOfflineSince() {
  const offlineSince = useStore(store, (state: State) => state.connectivity.offlineSince)

  const setOfflineSinceFn = (isOnline: boolean) => {
    setOfflineSince(isOnline ? undefined : new UTCDate().toISOString())
  }

  const isOfflineSinceTooLong = () => {
    if (offlineSince) {
      const secondsOffline = differenceInSeconds(new UTCDate(), parseISO(offlineSince))
      return secondsOffline >= OFFLINE_EXPIRY_CHECK_DURATION
    }
    return false
  }

  return {
    offlineSince,
    setOfflineSince: setOfflineSinceFn,
    isOfflineSinceTooLong
  }
}
