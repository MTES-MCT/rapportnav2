import { Icon } from '@mtes-mct/monitor-ui'
import { useStore } from '@tanstack/react-store'
import { useEffect, useState } from 'react'
import useAuth from '../v2/features/auth/hooks/use-auth'
import { ModuleType } from '../v2/features/common/types/module-type'
import { OwnerType } from '../v2/features/common/types/owner-type'
import { RoleType } from '../v2/features/common/types/role-type'
import { State, store } from '../v2/store'
import { setModuleType } from '../v2/store/slices/module-reducer'
import { loadImpersonationFromStorage } from '../v2/store/slices/impersonation-reducer'
import { LOGIN_PATH } from './routes'

type SidebarItem = {
  key: string
  url: string
  icon: any
}

const ROUTES = {
  [RoleType.ADMIN]: ModuleType.ADMIN,
  [RoleType.USER_PAM]: ModuleType.PAM,
  [RoleType.USER_ULAM]: ModuleType.ULAM
}

const MISSION_SIDEBAR = {
  key: 'missions',
  url: '/missions',
  icon: Icon.MissionAction
}

const INQUIRY_SIDEBAR = {
  key: 'inquiries',
  url: '/inquiries',
  icon: Icon.Archive
}
const ADMIN_SIDEBAR = {
  key: 'admin',
  url: '/admin',
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
  const module = useStore(store, (state: State) => state.module)

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
    let url = `/${module?.type}/${page.toString()}`
    const searchParams = getPageParams(params)
    if (searchParams.toString()) url += `?${searchParams.toString()}`
    return url
  }

  const getModuleTypeFromRoles = (roles?: RoleType[]) => {
    if (roles?.includes(RoleType.USER_PAM)) return ModuleType.PAM
    if (roles?.includes(RoleType.USER_ULAM)) return ModuleType.ULAM
    if (roles?.includes(RoleType.ADMIN)) return ModuleType.ADMIN
    return ModuleType.PAM
  }

  const getModuleTypeFromImpersonation = (): ModuleType | null => {
    const impersonation = loadImpersonationFromStorage()
    if (impersonation.isImpersonating && impersonation.targetServiceType) {
      return impersonation.targetServiceType === 'PAM' ? ModuleType.PAM : ModuleType.ULAM
    }
    return null
  }

  useEffect(() => {
    const getUrl = () => {
      if (!isAuthenticated) return LOGIN_PATH
      const user = isLoggedIn()

      // Check if impersonating - use impersonated service type
      const impersonatedModuleType = getModuleTypeFromImpersonation()
      const moduleType = impersonatedModuleType ?? getModuleTypeFromRoles(user?.roles)
      const page = moduleType === ModuleType.ADMIN ? '' : '/missions'
      const homeUrl = `/${moduleType}${page}`

      setHomeUrl(homeUrl)
      setModuleType(moduleType)

      return homeUrl
    }

    setHomeUrl(getUrl())
  }, [isAuthenticated, isLoggedIn])

  const getSideBar = (sideBar: SidebarItem, moduleType: ModuleType) => {
    return { ...sideBar, url: `/${moduleType}${sideBar.url}` }
  }

  const getSidebarItems = () => {
    const roles = isLoggedIn()?.roles

    // Check if impersonating - use impersonated service type for sidebar
    const impersonatedModuleType = getModuleTypeFromImpersonation()
    const isImpersonating = impersonatedModuleType !== null
    const effectiveModuleType = impersonatedModuleType ?? (roles?.includes(RoleType.USER_ULAM) ? ModuleType.ULAM : ModuleType.PAM)

    const SIDEBAR_ITEMS = [getSideBar(MISSION_SIDEBAR, effectiveModuleType)]

    // Show inquiries for ULAM (either by role or impersonation)
    if (effectiveModuleType === ModuleType.ULAM) {
      SIDEBAR_ITEMS.push(getSideBar(INQUIRY_SIDEBAR, effectiveModuleType))
    }

    // Always show admin link for admins (even when impersonating)
    if (roles?.includes(RoleType.ADMIN)) {
      SIDEBAR_ITEMS.push(ADMIN_SIDEBAR)
    }

    return SIDEBAR_ITEMS
  }

  return {
    getUrl,
    homeUrl,
    getSidebarItems
  }
}
