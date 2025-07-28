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
  getUrl: (page: OwnerType) => string
}

export function useGlobalRoutes(): RouteHook {
  const [homeUrl, setHomeUrl] = useState<string>()
  const { isLoggedIn, isAuthenticated } = useAuth()
  const { isV2, type } = useStore(store, (state: State) => state.module)

  const getUrl = (page: OwnerType) => {
    const baseUrl = isV2 ? '/v2' : ''
    return `${baseUrl}/${type}/${page.toString()}`
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
