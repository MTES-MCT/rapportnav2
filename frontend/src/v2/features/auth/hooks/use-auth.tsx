import AuthToken from '@features/auth/utils/token'
import { useQueryClient } from '@tanstack/react-query'
import { jwtDecode, JwtPayload } from 'jwt-decode'
import { useCallback, useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { RoleType } from '../../common/types/role-type'

type AuthHook = {
  isAuthenticated: boolean
  logout: () => Promise<void>
  isLoggedIn: () => Token | undefined
  navigateAndResetCache: (to: string, keys: string[]) => Promise<void>
}

export type Token = JwtPayload & { userId: number; roles: RoleType[] }

const authToken = new AuthToken()

// 🔑 global logout handler setter
let logoutHandler: (() => void) | null = null
export const setGlobalLogout = (fn: () => void) => {
  logoutHandler = fn
}
export const triggerGlobalLogout = () => {
  logoutHandler?.()
}

export const useAuth = (authTokenInstance: AuthToken = new AuthToken()): AuthHook => {
  const authToken = authTokenInstance
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(!!authToken.get())

  const logout = useCallback(async (): Promise<void> => {
    authToken.remove()
    setIsAuthenticated(false)

    // clear react-query cache
    queryClient.clear()

    navigate('/login', { replace: true })
  })

  const navigateAndResetCache = async (to: string, keys: string[] = []) => {
    keys.forEach(key => queryClient.invalidateQueries({ queryKey: [key.toString()] }))
    navigate(to)
  }

  const isLoggedIn = (): Token | undefined => {
    const token = authToken?.get()
    return token ? jwtDecode(token) : undefined
  }

  useEffect(() => {
    if (!!authToken.get() && !isAuthenticated) {
      setIsAuthenticated(true)
    }

    // ✅ register global logout
    setGlobalLogout(() => {
      void logout()
    })
  }, [isAuthenticated, logout])

  return { isAuthenticated, logout, navigateAndResetCache, isLoggedIn }
}

export default useAuth
