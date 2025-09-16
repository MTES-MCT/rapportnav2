import { useStore } from '@tanstack/react-store'
import { useEffect, useState } from 'react'
import useAuth from '../v2/features/auth/hooks/use-auth'
import { ModuleType } from '../v2/features/common/types/module-type'
import { OwnerType } from '../v2/features/common/types/owner-type'
import { RoleType } from '../v2/features/common/types/role-type'
import { State, store } from '../v2/store'
import { setModuleType } from '../v2/store/slices/module-reducer'
import { LOGIN_PATH } from './routes'

const ROUTES = {
  [RoleType.ADMIN]: ModuleType.ADMIN,
  [RoleType.USER_PAM]: ModuleType.PAM,
  [RoleType.USER_ULAM]: ModuleType.ULAM
}

type RouteHook = {
  homeUrl?: string
  getUrl: (page: OwnerType, params?: object) => string
}

export function useGlobalRoutes(): RouteHook {
  const [homeUrl, setHomeUrl] = useState<string>()
  const { isLoggedIn, isAuthenticated } = useAuth()
  const { isV2, type } = useStore(store, (state: State) => state.module)

  const getPageParams = (params?: object): URLSearchParams => {
    const searchParams = new URLSearchParams()
    if (params && Object.keys(params).length > 0) {
      // Add each param to the URLSearchParams
      Object.entries(params).forEach(([key, value]) => {
        if (value !== null && value !== undefined) {
          // Handle different value types appropriately
          if (value instanceof Date) {
            searchParams.append(key, value.toISOString())
          } else {
            searchParams.append(key, String(value))
          }
        }
      })
    }
    return searchParams
  }

  const getUrl = (page: OwnerType, params?: object) => {
    const baseUrl = isV2 ? '/v2' : ''
    let url = `${baseUrl}/${type}/${page.toString()}`

    const searchParams = getPageParams(params)
    // Append query string if there are params
    if (searchParams.toString()) {
      url += `?${searchParams.toString()}`
    }
    return url
  }

  useEffect(() => {
    const getUrl = () => {
      if (!isAuthenticated) return LOGIN_PATH
      const user = isLoggedIn()
      const baseUrl = isV2 ? '/v2' : ''
      const moduleType = ROUTES[user?.roles[0] ?? RoleType.USER_PAM]
      const page = moduleType === ModuleType.ADMIN ? 'crews' : 'missions'
      const homeUrl = `${baseUrl}/${moduleType}/${page}`

      setHomeUrl(homeUrl)
      setModuleType(moduleType)

      return homeUrl
    }

    setHomeUrl(getUrl())
  }, [isAuthenticated, isLoggedIn, isV2])

  return {
    getUrl,
    homeUrl
  }
}
