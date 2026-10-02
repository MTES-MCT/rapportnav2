import { describe, it, expect, vi, beforeEach, afterEach } from 'vitest'
import { render, screen, waitFor } from '../../../../../test-utils'
import * as Sentry from '@sentry/react'
import { useIsRestoring } from '@tanstack/react-query'
import AuthGuard from '../auth-guard'
import * as useUserModule from '../../../common/services/use-user'
import * as usePrefetchModule from '../../../common/services/use-prefetch-static-data'
import * as userReducer from '../../../../store/slices/user-reducer'

// Mock modules
vi.mock('@sentry/react')
vi.mock('react-router', async importOriginal => ({
  ...(await importOriginal<typeof import('react-router')>()),
  Navigate: ({ to }: { to: string }) => <div>REDIRECTED:{to}</div>
}))
vi.mock('@tanstack/react-query', async importOriginal => ({
  ...(await importOriginal<typeof import('@tanstack/react-query')>()),
  useIsRestoring: vi.fn(() => false)
}))
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

    // Default: persisted cache already restored
    vi.mocked(useIsRestoring).mockReturnValue(false)
  })

  afterEach(() => {
    vi.clearAllMocks()
  })

  // Drive the guard via the `users/me` query: authenticated when it returns a user,
  // denied when it errors (401/403).
  const renderWithUser = (isAuthenticated: boolean, user?: any) => {
    vi.mocked(useUserModule.useGetCurrentUserQuery).mockReturnValue({
      data: isAuthenticated ? (user ?? { id: 1 }) : undefined,
      isLoading: false,
      isError: !isAuthenticated
    } as any)

    return render(
      <AuthGuard>
        <div>Protected Content</div>
      </AuthGuard>
    )
  }

  describe('Authentication', () => {
    it('should render children when authenticated', () => {
      renderWithUser(true, { id: 1 })
      expect(screen.getByText('Protected Content')).toBeInTheDocument()
    })

    it('should redirect to login when not authenticated', () => {
      renderWithUser(false)
      expect(screen.queryByText('Protected Content')).toBeNull()
      expect(screen.getByText('REDIRECTED:/login')).toBeInTheDocument()
    })

    it('should render nothing while the who-am-I call is in flight', () => {
      vi.mocked(useUserModule.useGetCurrentUserQuery).mockReturnValue({
        data: undefined,
        isLoading: true,
        isError: false
      } as any)

      render(
        <AuthGuard>
          <div>Protected Content</div>
        </AuthGuard>
      )
      expect(screen.queryByText('Protected Content')).toBeNull()
    })

    it('should NOT redirect while the persisted cache is restoring (paused query: isLoading false, no user)', () => {
      // Reproduces the "sign in twice" glitch: during cache restoration the query is paused, so
      // isLoading is false and user is undefined. We must wait, not bounce to /login.
      vi.mocked(useIsRestoring).mockReturnValue(true)
      vi.mocked(useUserModule.useGetCurrentUserQuery).mockReturnValue({
        data: undefined,
        isLoading: false,
        isError: false
      } as any)

      render(
        <AuthGuard>
          <div>Protected Content</div>
        </AuthGuard>
      )

      // Neither granted (no user yet) nor denied (no redirect) — it renders nothing and waits.
      expect(screen.queryByText('Protected Content')).toBeNull()
      expect(screen.queryByText('REDIRECTED:/login')).toBeNull()
    })
  })

  describe('Sentry User Context', () => {
    it('should set Sentry user ID when authenticated', async () => {
      const user = { id: 42, serviceId: 123 }
      renderWithUser(true, user)

      await waitFor(() => {
        expect(mockSetUser).toHaveBeenCalledWith({ id: '42' })
      })
    })

    it('should set service_id tag when user has serviceId', async () => {
      const user = { id: 1, serviceId: 456 }
      renderWithUser(true, user)

      await waitFor(() => {
        expect(mockSetTag).toHaveBeenCalledWith('service_id', '456')
      })
    })

    it('should NOT set service_id tag when user has no serviceId', async () => {
      const user = { id: 1, serviceId: undefined }
      renderWithUser(true, user)

      await waitFor(() => {
        expect(mockSetUser).toHaveBeenCalledWith({ id: '1' })
      })

      // service_id should not be set
      expect(mockSetTag).not.toHaveBeenCalledWith('service_id', expect.anything())
    })

    it('should clear Sentry user when not authenticated', async () => {
      renderWithUser(false)

      await waitFor(() => {
        expect(mockSetUser).toHaveBeenCalledWith(null)
      })
    })

    it('should remove service_id tag when not authenticated', async () => {
      renderWithUser(false)

      await waitFor(() => {
        expect(mockSetTag).toHaveBeenCalledWith('service_id', undefined)
      })
    })
  })

  describe('Context Cleanup', () => {
    it('should clear user context when user logs out', async () => {
      vi.mocked(useUserModule.useGetCurrentUserQuery).mockReturnValue({
        data: { id: 1 },
        isLoading: false,
        isError: false
      } as any)

      const { rerender } = render(
        <AuthGuard>
          <div>Protected Content</div>
        </AuthGuard>
      )

      // Simulate logout by changing the who-am-I result to unauthenticated
      vi.mocked(useUserModule.useGetCurrentUserQuery).mockReturnValue({
        data: undefined,
        isLoading: false,
        isError: true
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
