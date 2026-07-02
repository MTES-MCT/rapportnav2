import { useSelector } from '@tanstack/react-store'
import { store } from '../../../store'

const _satiEnvRaw: string = import.meta.env.SATI_ENABLED_SERVICES ?? ''
export const SATI_ENABLED_SERVICES: number[] = _satiEnvRaw
  .split(',')
  .map(Number)
  .filter(n => !isNaN(n) && n > 0)

interface SatiHook {
  isSatiEnabled: boolean
}

export function useSati(): SatiHook {
  const user = useSelector(store, state => state.user)

  const isServiceIdIsEnabled = (): boolean => {
    const serviceId = user?.serviceId
    return !!serviceId && SATI_ENABLED_SERVICES.includes(serviceId)
  }

  return { isSatiEnabled: isServiceIdIsEnabled() }
}
