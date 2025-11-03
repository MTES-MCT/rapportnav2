import { Icon } from '@mtes-mct/monitor-ui'
import { useStore } from '@tanstack/react-store'
import { useEffect, useState } from 'react'
import useAuth from '../v2/features/auth/hooks/use-auth'
import { ModuleType } from '../v2/features/common/types/module-type'
import { OwnerType } from '../v2/features/common/types/owner-type'
import { RoleType } from '../v2/features/common/types/role-type'
import { State, store } from '../v2/store'
import { setModuleType } from '../v2/store/slices/module-reducer'
import { LOGIN_PATH } from './routes'

type SidebarItem = {
  key: string
  url: OwnerType
  icon: any
}

const ROUTES = {
  [RoleType.ADMIN]: ModuleType.ADMIN,
  [RoleType.USER_PAM]: ModuleType.PAM,
  [RoleType.USER_ULAM]: ModuleType.ULAM
}

const MISSION_SIDEBAR = {
  key: 'list',
  url: OwnerType.MISSION,
  icon: Icon.MissionAction
}

const INQUIRY_SIDEBAR = {
  key: 'inquiries',
  url: OwnerType.INQUIRY,
  icon: Icon.Archive
}
const ADMIN_SIDEBAR = {
  key: 'admin',
  url: OwnerType.ADMIN,
  icon: Icon.GroupPerson
}

type RouteHook = {
  homeUrl?: string
  getSidebarItems: () => SidebarItem[]
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
    if (page === OwnerType.ADMIN) return `${baseUrl}/${page.toString()}`

    let url = `${baseUrl}/${type}/${page.toString()}`
    const searchParams = getPageParams(params)
    if (searchParams.toString()) url += `?${searchParams.toString()}`
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

  const getSidebarItems = () => {
    const roles = isLoggedIn()?.roles
    const SIDEBAR_ITEMS = [MISSION_SIDEBAR]
    if (roles?.includes(RoleType.USER_ULAM)) SIDEBAR_ITEMS.push(INQUIRY_SIDEBAR)
    if (roles?.includes(RoleType.ADMIN)) SIDEBAR_ITEMS.push(ADMIN_SIDEBAR)
    return SIDEBAR_ITEMS
  }

  return {
    getUrl,
    homeUrl,
    getSidebarItems
  }
}
