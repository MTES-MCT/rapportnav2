import { beforeEach, describe, expect, it, vi } from 'vitest'
import { render, screen } from '../../../../../test-utils'
import { RoleType } from '../../../common/types/role-type'
import * as useAuthModule from '../../hooks/use-auth'
import RoleGuard from '../role-guard'

// Mock modules
vi.mock('../../hooks/use-auth')

describe('RoleGuard', () => {
  beforeEach(() => {
    vi.clearAllMocks()
  })

  const getUseAuth = {
    isAuthenticated: true,
    isLoggedIn: vi.fn(),
    logout: vi.fn(),
    navigateAndResetCache: vi.fn(),
    roleOptions: [],
    hasRoles: vi.fn()
  }

  describe('Authorization', () => {
    it('should render children when user has role', () => {
      vi.mocked(useAuthModule.default).mockReturnValue({ ...getUseAuth, hasRoles: () => true })
      render(
        <RoleGuard roles={[RoleType.ADMIN]}>
          <div>Admin Content</div>
        </RoleGuard>
      )
      expect(screen.getByText('Admin Content')).toBeInTheDocument()
    })

    it('should redirect when user has not  role only', () => {
      vi.mocked(useAuthModule.default).mockReturnValue({ ...getUseAuth, hasRoles: () => false })
      render(
        <RoleGuard roles={[RoleType.ADMIN]}>
          <div>Admin Content</div>
        </RoleGuard>
      )
      expect(screen.queryByText('Admin Content')).toBeNull()
    })
  })
})
