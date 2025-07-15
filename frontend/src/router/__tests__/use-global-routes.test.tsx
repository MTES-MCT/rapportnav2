import { useStore } from '@tanstack/react-store'
import { renderHook } from '@testing-library/react'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import useAuth, { Token } from '../../v2/features/auth/hooks/use-auth'
import { ModuleType } from '../../v2/features/common/types/module-type'
import { RoleType } from '../../v2/features/common/types/role-type'
import { setModuleType } from '../../v2/store/slices/module-reducer.ts'
import { LOGIN_PATH } from '../routes'
import { useGlobalRoutes } from '../use-global-routes'

vi.mock('@tanstack/react-store', () => ({
  useStore: vi.fn()
}))
vi.mock('../../v2/features/auth/hooks/use-auth', () => ({
  __esModule: true,
  default: vi.fn()
}))
vi.mock('../../v2/store/slices/module-reducer', () => ({
  setModuleType: vi.fn()
}))

const mockUseStore = vi.mocked(useStore)
const mockUseAuth = vi.mocked(useAuth)
const mockSetModuleType = vi.mocked(setModuleType)

describe('useGlobalRoutes', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  it('returns LOGIN_PATH if not authenticated', () => {
    mockUseAuth.mockReturnValue({
      isAuthenticated: false,
      isLoggedIn: vi.fn(),
      logout: vi.fn(),
      navigateAndResetCache: vi.fn()
    })
    mockUseStore.mockReturnValue({ isV2: false })

    const { result } = renderHook(() => useGlobalRoutes())
    expect(result.current.homeUrl).toBe(LOGIN_PATH)
  })

  it('returns /v2/admin/crews for ADMIN role and v2', () => {
    mockUseAuth.mockReturnValue({
      isAuthenticated: true,
      isLoggedIn: () => ({ roles: [RoleType.ADMIN], userId: 2 }) as Token,
      logout: vi.fn(),
      navigateAndResetCache: vi.fn()
    })
    mockUseStore.mockReturnValue({ isV2: true })

    const { result } = renderHook(() => useGlobalRoutes())
    expect(result.current.homeUrl).toBe('/v2/admin/crews')
    expect(mockSetModuleType).toHaveBeenCalledWith(ModuleType.ADMIN)
  })

  it('returns /pam/missions for USER_PAM role and not v2', () => {
    mockUseAuth.mockReturnValue({
      isAuthenticated: true,
      isLoggedIn: () => ({ roles: [RoleType.USER_PAM], userId: 2 }) as Token,
      logout: vi.fn(),
      navigateAndResetCache: vi.fn()
    })
    mockUseStore.mockReturnValue({ isV2: false })

    const { result } = renderHook(() => useGlobalRoutes())
    expect(result.current.homeUrl).toBe('/pam/missions')
    expect(mockSetModuleType).toHaveBeenCalledWith(ModuleType.PAM)
  })

  it('returns /ulam/missions for USER_ULAM role and not v2', () => {
    mockUseAuth.mockReturnValue({
      isAuthenticated: true,
      isLoggedIn: () => ({ roles: [RoleType.USER_ULAM], userId: 2 }) as Token,
      logout: vi.fn(),
      navigateAndResetCache: vi.fn()
    })
    mockUseStore.mockReturnValue({ isV2: false })

    const { result } = renderHook(() => useGlobalRoutes())
    expect(result.current.homeUrl).toBe('/ulam/missions')
    expect(mockSetModuleType).toHaveBeenCalledWith(ModuleType.ULAM)
  })

  it('returns /v2/pam/missions for USER_PAM role and v2', () => {
    mockUseAuth.mockReturnValue({
      isAuthenticated: true,
      isLoggedIn: () => ({ roles: [RoleType.USER_PAM], userId: 2 }) as Token,
      logout: vi.fn(),
      navigateAndResetCache: vi.fn()
    })
    mockUseStore.mockReturnValue({ isV2: true })

    const { result } = renderHook(() => useGlobalRoutes())
    expect(result.current.homeUrl).toBe('/v2/pam/missions')
    expect(mockSetModuleType).toHaveBeenCalledWith(ModuleType.PAM)
  })

  it('defaults to PAM if user has no roles', () => {
    mockUseAuth.mockReturnValue({
      isAuthenticated: true,
      isLoggedIn: () => ({ roles: [], userId: 2 }) as Token,
      logout: vi.fn(),
      navigateAndResetCache: vi.fn()
    })
    mockUseStore.mockReturnValue({ isV2: false })

    const { result } = renderHook(() => useGlobalRoutes())
    expect(result.current.homeUrl).toBe('/pam/missions')
    expect(mockSetModuleType).toHaveBeenCalledWith(ModuleType.PAM)
  })

  it('handles missing user object from isLoggedIn', () => {
    mockUseAuth.mockReturnValue({
      isAuthenticated: true,
      isLoggedIn: () => undefined,
      logout: vi.fn(),
      navigateAndResetCache: vi.fn()
    })
    mockUseStore.mockReturnValue({ isV2: false })

    const { result } = renderHook(() => useGlobalRoutes())
    expect(result.current.homeUrl).toBe('/pam/missions')
    expect(mockSetModuleType).toHaveBeenCalledWith(ModuleType.PAM)
  })
})
