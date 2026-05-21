import AuthToken from '@features/auth/utils/token'
import { useQueryClient } from '@tanstack/react-query'
import { jwtDecode, JwtPayload } from 'jwt-decode'
import { intersection } from 'lodash'
import { useCallback, useEffect, useRef, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { RoleType } from '../../common/types/role-type'

type AuthHook = {
  isAuthenticated: boolean
  logout: () => Promise<void>
  isLoggedIn: () => Token | undefined
  hasRoles: (roles: RoleType[]) => boolean
  roleOptions: { label: string; value: RoleType }[]
  navigateAndResetCache: (to: string, keys: string[]) => Promise<void>
}

const ROLE_REGISTRY = {
  [RoleType.ADMIN]: 'Admin',
  [RoleType.USER_PAM]: 'User PAM',
  [RoleType.USER_ULAM]: 'User ULAM',
  [RoleType.MANAGER_PAM]: 'Manager PAM',
  [RoleType.MANAGER_ULAM]: 'Manager ULAM'
}

export type Token = JwtPayload & { userId: number; roles: RoleType[] }

// 🔑 global logout handler setter
let logoutHandler: (() => void) | null = null
export const setGlobalLogout = (fn: () => void) => {
  logoutHandler = fn
}
export const triggerGlobalLogout = () => {
  logoutHandler?.()
}

export const useAuth = (authTokenInstance: AuthToken = new AuthToken()): AuthHook => {
  const authTokenRef = useRef(authTokenInstance)
  const authToken = authTokenRef.current
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(!!authToken.get())

  const logout = useCallback(async (): Promise<void> => {
    authToken.remove()
    setIsAuthenticated(false)

    // clear react-query cache
    queryClient.clear()

    navigate('/login', { replace: true })
  }, [authToken, queryClient, navigate])

  const navigateAndResetCache = async (to: string, keys: string[] = []) => {
    keys.forEach(key => queryClient.invalidateQueries({ queryKey: [key.toString()] }))
    navigate(to)
  }

  const isLoggedIn = useCallback((): Token | undefined => {
    const token = authToken?.get()
    return token ? jwtDecode(token) : undefined
  }, [authToken])

  const hasRoles = useCallback(
    (roles: RoleType[]): boolean => {
      const token = authToken?.get()
      if (!token) return false
      return intersection((jwtDecode(token) as Token)?.roles, roles).length > 0
    },
    [authToken]
  )

  useEffect(() => {
    if (!!authToken.get() && !isAuthenticated) {
      setIsAuthenticated(true)
    }

    // ✅ register global logout
    setGlobalLogout(() => {
      void logout()
    })
  }, [isAuthenticated, logout])

  const getRoleOptions = () =>
    Object.keys(RoleType)?.map(key => ({
      value: RoleType[key as keyof typeof RoleType],
      label: ROLE_REGISTRY[key as keyof typeof RoleType]
    }))

  return { hasRoles, isAuthenticated, logout, navigateAndResetCache, isLoggedIn, roleOptions: getRoleOptions() }
}

export default useAuth
