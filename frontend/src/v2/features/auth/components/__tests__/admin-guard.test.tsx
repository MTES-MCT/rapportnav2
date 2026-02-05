import { describe, it, expect, vi, beforeEach } from 'vitest'
import { render, screen } from '../../../../../test-utils'
import AdminGuard from '../admin-guard'
import * as useAuthModule from '../../hooks/use-auth'
import { RoleType } from '../../../common/types/role-type'

// Mock modules
vi.mock('../../hooks/use-auth')

describe('AdminGuard', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  const renderWithRole = (roles: RoleType[] | undefined) => {
    vi.mocked(useAuthModule.default).mockReturnValue({
      isAuthenticated: roles !== undefined,
      isLoggedIn: () => (roles !== undefined ? { userId: 1, roles } : undefined),
      logout: vi.fn(),
      navigateAndResetCache: vi.fn()
    })

    return render(
      <AdminGuard>
        <div>Admin Content</div>
      </AdminGuard>
    )
  }

  describe('Authorization', () => {
    it('should render children when user has ADMIN role', () => {
      renderWithRole([RoleType.ADMIN])
      expect(screen.getByText('Admin Content')).toBeInTheDocument()
    })

    it('should render children when user has ADMIN role among other roles', () => {
      renderWithRole([RoleType.USER_PAM, RoleType.ADMIN])
      expect(screen.getByText('Admin Content')).toBeInTheDocument()
    })

    it('should redirect when user has USER_PAM role only', () => {
      renderWithRole([RoleType.USER_PAM])
      expect(screen.queryByText('Admin Content')).toBeNull()
    })

    it('should redirect when user has USER_ULAM role only', () => {
      renderWithRole([RoleType.USER_ULAM])
      expect(screen.queryByText('Admin Content')).toBeNull()
    })

    it('should redirect when user is not logged in', () => {
      renderWithRole(undefined)
      expect(screen.queryByText('Admin Content')).toBeNull()
    })
  })
})