import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { render, screen, waitFor } from '../../../../../test-utils'
import { MemoryRouter, Routes, Route } from 'react-router-dom'
import * as Sentry from '@sentry/react'
import AuthGuard from '../auth-guard'
import * as useAuthModule from '../../hooks/use-auth'
import * as useUserModule from '../../../common/services/use-user'
import * as usePrefetchModule from '../../../common/services/use-prefetch-static-data'
import * as userReducer from '../../../../store/slices/user-reducer'

// Mock modules
vi.mock('@sentry/react')
vi.mock('../../hooks/use-auth')
vi.mock('../../../common/services/use-user')
vi.mock('../../../common/services/use-prefetch-static-data')
vi.mock('../../../../store/slices/user-reducer')

describe('AuthGuard', () => {
  const mockSetUser = vi.fn()
  const mockSetTag = vi.fn()

  beforeEach(() => {
    vi.clearAllMocks()

    // Mock Sentry
    vi.mocked(Sentry.setUser).mockImplementation(mockSetUser)
    vi.mocked(Sentry.setTag).mockImplementation(mockSetTag)

    // Mock prefetch
    vi.mocked(usePrefetchModule.usePrefetchStaticData).mockReturnValue(undefined)

    // Mock setUser reducer
    vi.mocked(userReducer.setUser).mockImplementation(() => {})
  })

  afterEach(() => {
    vi.clearAllMocks()
  })

  const renderWithRouter = (isAuthenticated: boolean, user?: any) => {
    vi.mocked(useAuthModule.default).mockReturnValue({
      isAuthenticated,
      isLoggedIn: () => (isAuthenticated ? { userId: user?.id || 1, roles: ['USER'] } : undefined),
      logout: vi.fn(),
      navigateAndResetCache: vi.fn()
    })

    vi.mocked(useUserModule.default).mockReturnValue({
      data: user,
      isLoading: false,
      error: null
    } as any)

    return render(
      <AuthGuard>
        <div>Protected Content</div>
      </AuthGuard>
    )
  }

  describe('Authentication', () => {
    it('should render children when authenticated', () => {
      renderWithRouter(true, { id: 1 })
      expect(screen.getByText('Protected Content')).toBeInTheDocument()
    })

    it('should redirect to login when not authenticated', () => {
      renderWithRouter(false)
      expect(screen.queryByText('Protected Content')).toBeNull()
    })
  })

  describe('Sentry User Context', () => {
    it('should set Sentry user ID when authenticated', async () => {
      const user = { id: 42, serviceId: 123 }
      renderWithRouter(true, user)

      await waitFor(() => {
        expect(mockSetUser).toHaveBeenCalledWith({ id: '42' })
      })
    })

    it('should set service_id tag when user has serviceId', async () => {
      const user = { id: 1, serviceId: 456 }
      renderWithRouter(true, user)

      await waitFor(() => {
        expect(mockSetTag).toHaveBeenCalledWith('service_id', '456')
      })
    })

    it('should NOT set service_id tag when user has no serviceId', async () => {
      const user = { id: 1, serviceId: undefined }
      renderWithRouter(true, user)

      await waitFor(() => {
        expect(mockSetUser).toHaveBeenCalledWith({ id: '1' })
      })

      // service_id should not be set
      expect(mockSetTag).not.toHaveBeenCalledWith('service_id', expect.anything())
    })

    it('should clear Sentry user when not authenticated', async () => {
      renderWithRouter(false)

      await waitFor(() => {
        expect(mockSetUser).toHaveBeenCalledWith(null)
      })
    })

    it('should remove service_id tag when not authenticated', async () => {
      renderWithRouter(false)

      await waitFor(() => {
        expect(mockSetTag).toHaveBeenCalledWith('service_id', undefined)
      })
    })
  })

  describe('Context Cleanup', () => {
    it('should clear user context when user logs out', async () => {
      // First render as authenticated
      const { rerender } = render(
        <AuthGuard>
          <div>Protected Content</div>
        </AuthGuard>
      )

      // Simulate logout by changing auth state
      vi.mocked(useAuthModule.default).mockReturnValue({
        isAuthenticated: false,
        isLoggedIn: () => undefined,
        logout: vi.fn(),
        navigateAndResetCache: vi.fn()
      })

      vi.mocked(useUserModule.default).mockReturnValue({
        data: undefined,
        isLoading: false,
        error: null
      } as any)

      rerender(
        <AuthGuard>
          <div>Protected Content</div>
        </AuthGuard>
      )

      await waitFor(() => {
        expect(mockSetUser).toHaveBeenCalledWith(null)
        expect(mockSetTag).toHaveBeenCalledWith('service_id', undefined)
      })
    })
  })
})
