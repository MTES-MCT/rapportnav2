import AuthToken from '@features/auth/utils/token'
import { useQueryClient } from '@tanstack/react-query'
import { useSelector } from '@tanstack/react-store'
import { jwtDecode, JwtPayload } from 'jwt-decode'
import { intersection } from 'lodash'
import { useCallback, useEffect, useMemo, useRef } from 'react'
import { useNavigate } from 'react-router'
import { RoleType } from '../../common/types/role-type'
import { State, store } from '../../../store'
import { setUser } from '../../../store/slices/user-reducer'

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

// Full-page POST to the backend logout endpoint (CSRF is disabled). Ends both the local
// session and the ProConnect IdP session, then the backend redirects to /login.
const submitBackendLogout = () => {
  const form = document.createElement('form')
  form.method = 'POST'
  form.action = '/logout'
  document.body.appendChild(form)
  form.submit()
}

export const useAuth = (authTokenInstance: AuthToken = new AuthToken()): AuthHook => {
  const authTokenRef = useRef(authTokenInstance)
  const authToken = authTokenRef.current
  const navigate = useNavigate()
  const queryClient = useQueryClient()

  // The current user is populated by AuthGuard from `users/me` and works for both auth
  // methods. For legacy JWT/password users, the token (localStorage) remains the source
  // of truth so behaviour is unchanged; ProConnect session users have no token and rely
  // solely on the store.
  const storeUser = useSelector(store, (state: State) => state.user)

  const decodedToken = useCallback((): Token | undefined => {
    const token = authToken?.get()
    return token ? (jwtDecode(token) as Token) : undefined
  }, [authToken])

  const isAuthenticated = !!authToken.get() || !!storeUser?.id

  const logout = useCallback(async (): Promise<void> => {
    const hadToken = !!authToken.get()
    authToken.remove()
    setUser(undefined)

    // clear react-query cache
    queryClient.clear()

    if (!hadToken && import.meta.env.FRONTEND_OIDC_ENABLED === 'true') {
      // ProConnect session user: end the backend + IdP session server-side.
      submitBackendLogout()
    } else {
      // Legacy JWT/password user (stateless) or OIDC disabled: just go back to login.
      navigate('/login', { replace: true })
    }
  }, [authToken, queryClient, navigate])

  const navigateAndResetCache = async (to: string, keys: string[] = []) => {
    keys.forEach(key => queryClient.invalidateQueries({ queryKey: [key.toString()] }))
    navigate(to)
  }

  const isLoggedIn = useCallback((): Token | undefined => {
    const token = decodedToken()
    if (token) return token
    if (storeUser?.id) {
      return { userId: storeUser.id, roles: storeUser.roles ?? [] } as Token
    }
    return undefined
  }, [decodedToken, storeUser])

  const hasRoles = useCallback(
    (roles: RoleType[]): boolean => {
      const currentRoles = decodedToken()?.roles ?? storeUser?.roles ?? []
      return intersection(currentRoles, roles).length > 0
    },
    [decodedToken, storeUser]
  )

  useEffect(() => {
    // ✅ register global logout
    setGlobalLogout(() => {
      void logout()
    })
  }, [logout])

  const roleOptions = useMemo(
    () =>
      Object.keys(RoleType)?.map(key => ({
        value: RoleType[key as keyof typeof RoleType],
        label: ROLE_REGISTRY[key as keyof typeof RoleType]
      })),
    []
  )

  return { hasRoles, isAuthenticated, logout, navigateAndResetCache, isLoggedIn, roleOptions }
}

export default useAuth
