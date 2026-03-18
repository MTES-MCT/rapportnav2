import { useStore } from '@tanstack/react-store'
import { useQueryClient } from '@tanstack/react-query'
import { useCallback, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import { store } from '../../../store'
import {
  setImpersonation,
  clearImpersonation,
  loadImpersonationFromStorage
} from '../../../store/slices/impersonation-reducer'
import { setModuleType } from '../../../store/slices/module-reducer'
import { ModuleType } from '../../common/types/module-type'
import { RoleType } from '../../common/types/role-type'
import useAuth from '../../auth/hooks/use-auth'
import { ServiceType } from '../types/impersonation-types'

const serviceTypeToModuleType = (serviceType: ServiceType): ModuleType => {
  return serviceType === 'PAM' ? ModuleType.PAM : ModuleType.ULAM
}

export const useImpersonation = () => {
  const queryClient = useQueryClient()
  const navigate = useNavigate()
  const { isLoggedIn } = useAuth()
  const impersonation = useStore(store, state => state.impersonation)

  // Load impersonation state from storage on mount and set module type
  useEffect(() => {
    const storedState = loadImpersonationFromStorage()
    if (storedState.isImpersonating && storedState.targetServiceType) {
      store.setState(currentState => ({
        ...currentState,
        impersonation: storedState
      }))
      // Also restore the module type based on impersonated service
      setModuleType(serviceTypeToModuleType(storedState.targetServiceType))
    }
  }, [])

  const token = isLoggedIn()
  const canImpersonate = token?.roles?.includes(RoleType.ADMIN) ?? false

  const startImpersonation = useCallback(
    (serviceId: number, serviceName: string, serviceType: ServiceType) => {
      setImpersonation(serviceId, serviceName, serviceType)

      // Update module type based on service type
      const moduleType = serviceTypeToModuleType(serviceType)
      setModuleType(moduleType)

      // Invalidate all queries to refetch with new service context
      queryClient.invalidateQueries()

      // Navigate to the correct module's missions page
      navigate(`/${moduleType}/missions`, { replace: true })
    },
    [queryClient, navigate]
  )

  const stopImpersonation = useCallback(() => {
    clearImpersonation()

    // Reset module type to admin (since only admins can impersonate)
    setModuleType(ModuleType.ADMIN)

    // Invalidate all queries to refetch with original service context
    queryClient.invalidateQueries()

    // Navigate back to admin
    navigate('/', { replace: true })
  }, [queryClient, navigate])

  return {
    isImpersonating: impersonation.isImpersonating,
    targetServiceId: impersonation.targetServiceId,
    targetServiceName: impersonation.targetServiceName,
    targetServiceType: impersonation.targetServiceType,
    canImpersonate,
    startImpersonation,
    stopImpersonation
  }
}

export default useImpersonation
