import { useStore } from '@tanstack/react-store'
import { renderHook } from '@testing-library/react'
import { beforeEach, describe, expect, it, vi } from 'vitest'
import useAuth, { Token } from '../../v2/features/auth/hooks/use-auth'
import { ModuleType } from '../../v2/features/common/types/module-type'
import { RoleType } from '../../v2/features/common/types/role-type'
import { setModuleType } from '../../v2/store/slices/module-reducer.ts'
import { LOGIN_PATH } from '../routes'
import { useGlobalRoutes } from '../use-global-routes'
import { OwnerType } from '../../v2/features/common/types/owner-type.ts'

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

  describe('getUrl function', () => {
    beforeEach(() => {
      // Setup authenticated user for getUrl tests
      mockUseAuth.mockReturnValue({
        isAuthenticated: true,
        isLoggedIn: () => ({ roles: [RoleType.USER_PAM], userId: 2 }) as Token,
        logout: vi.fn(),
        navigateAndResetCache: vi.fn()
      })
    })

    it('generates URL without params for v1', () => {
      mockUseStore.mockReturnValue({ isV2: false, type: 'pam' })

      const { result } = renderHook(() => useGlobalRoutes())
      const url = result.current.getUrl(OwnerType.MISSION)

      expect(url).toBe('/pam/missions')
    })

    it('generates URL without params for v2', () => {
      mockUseStore.mockReturnValue({ isV2: true, type: 'pam' })

      const { result } = renderHook(() => useGlobalRoutes())
      const url = result.current.getUrl(OwnerType.MISSION)

      expect(url).toBe('/v2/pam/missions')
    })

    it('generates URL with simple string params', () => {
      mockUseStore.mockReturnValue({ isV2: false, type: 'pam' })

      const { result } = renderHook(() => useGlobalRoutes())
      const params = { status: 'active', search: 'test' }
      const url = result.current.getUrl(OwnerType.MISSION, params)

      expect(url).toBe('/pam/missions?status=active&search=test')
    })

    it('generates URL with Date params converted to ISO string', () => {
      mockUseStore.mockReturnValue({ isV2: true, type: 'pam' })

      const { result } = renderHook(() => useGlobalRoutes())
      const startDate = new Date('2025-01-01T00:00:00.000Z')
      const endDate = new Date('2025-12-31T23:59:59.999Z')
      const params = {
        startDateTimeUtc: startDate,
        endDateTimeUtc: endDate
      }
      const url = result.current.getUrl(OwnerType.MISSION, params)

      expect(url).toBe(
        '/v2/pam/missions?startDateTimeUtc=2025-01-01T00%3A00%3A00.000Z&endDateTimeUtc=2025-12-31T23%3A59%3A59.999Z'
      )
    })

    it('generates URL with mixed param types', () => {
      mockUseStore.mockReturnValue({ isV2: false, type: 'ulam' })

      const { result } = renderHook(() => useGlobalRoutes())
      const params = {
        status: 'active',
        count: 10,
        date: new Date('2025-06-15T12:00:00.000Z'),
        flag: true
      }
      const url = result.current.getUrl(OwnerType.MISSION, params)

      expect(url).toBe('/ulam/missions?status=active&count=10&date=2025-06-15T12%3A00%3A00.000Z&flag=true')
    })

    it('filters out null and undefined params', () => {
      mockUseStore.mockReturnValue({ isV2: false, type: 'pam' })

      const { result } = renderHook(() => useGlobalRoutes())
      const params = {
        status: 'active',
        nullValue: null,
        undefinedValue: undefined,
        search: 'test'
      }
      const url = result.current.getUrl(OwnerType.MISSION, params)

      expect(url).toBe('/pam/missions?status=active&search=test')
    })

    it('handles empty params object', () => {
      mockUseStore.mockReturnValue({ isV2: true, type: 'admin' })

      const { result } = renderHook(() => useGlobalRoutes())
      const url = result.current.getUrl(OwnerType.MISSION, {})

      expect(url).toBe('/v2/admin/missions')
    })

    it('handles params with special characters that need encoding', () => {
      mockUseStore.mockReturnValue({ isV2: false, type: 'pam' })

      const { result } = renderHook(() => useGlobalRoutes())
      const params = {
        search: 'test & value',
        filter: 'type=mission'
      }
      const url = result.current.getUrl(OwnerType.MISSION, params)

      expect(url).toBe('/pam/missions?search=test+%26+value&filter=type%3Dmission')
    })

    it('works with different OwnerType values', () => {
      mockUseStore.mockReturnValue({ isV2: true, type: 'admin' })

      const { result } = renderHook(() => useGlobalRoutes())

      const missionUrl = result.current.getUrl(OwnerType.MISSION, { type: 'active' })
      const inquiryUrl = result.current.getUrl(OwnerType.INQUIRY, { status: 'available' })

      expect(missionUrl).toBe('/v2/admin/missions?type=active')
      expect(inquiryUrl).toBe('/v2/admin/inquiries?status=available')
    })

    it('handles numeric zero values correctly', () => {
      mockUseStore.mockReturnValue({ isV2: false, type: 'pam' })

      const { result } = renderHook(() => useGlobalRoutes())
      const params = {
        page: 0,
        count: 0,
        status: 'active'
      }
      const url = result.current.getUrl(OwnerType.MISSION, params)

      expect(url).toBe('/pam/missions?page=0&count=0&status=active')
    })

    it('handles boolean false values correctly', () => {
      mockUseStore.mockReturnValue({ isV2: true, type: 'pam' })

      const { result } = renderHook(() => useGlobalRoutes())
      const params = {
        archived: false,
        active: true,
        status: 'pending'
      }
      const url = result.current.getUrl(OwnerType.MISSION, params)

      expect(url).toBe('/v2/pam/missions?archived=false&active=true&status=pending')
    })

    it('handles array values by converting to string', () => {
      mockUseStore.mockReturnValue({ isV2: false, type: 'ulam' })

      const { result } = renderHook(() => useGlobalRoutes())
      const params = {
        ids: [1, 2, 3],
        tags: ['urgent', 'priority']
      }
      const url = result.current.getUrl(OwnerType.MISSION, params)

      expect(url).toBe('/ulam/missions?ids=1%2C2%2C3&tags=urgent%2Cpriority')
    })
  })
})
