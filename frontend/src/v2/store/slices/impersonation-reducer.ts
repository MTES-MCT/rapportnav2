import { store } from '..'
import { ImpersonationState, ServiceType } from '../../features/impersonation/types/impersonation-types'

const STORAGE_KEY = 'impersonation'

export const setImpersonation = (
  targetServiceId: number,
  targetServiceName: string,
  targetServiceType: ServiceType
) => {
  const state: ImpersonationState = {
    isImpersonating: true,
    targetServiceId,
    targetServiceName,
    targetServiceType
  }
  sessionStorage.setItem(STORAGE_KEY, JSON.stringify(state))
  store.setState(currentState => ({
    ...currentState,
    impersonation: state
  }))
}

export const clearImpersonation = () => {
  sessionStorage.removeItem(STORAGE_KEY)
  store.setState(currentState => ({
    ...currentState,
    impersonation: {
      isImpersonating: false
    }
  }))
}

export const loadImpersonationFromStorage = (): ImpersonationState => {
  try {
    const stored = sessionStorage.getItem(STORAGE_KEY)
    if (stored) {
      return JSON.parse(stored) as ImpersonationState
    }
  } catch (e) {
    console.error('Failed to load impersonation state from storage', e)
  }
  return { isImpersonating: false }
}
