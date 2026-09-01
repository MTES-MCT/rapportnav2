import { LOGIN_PATH, ROOT_PATH, SIGNUP_PATH } from '@router/routes.tsx'
import { wrapCreateBrowserRouterV6 } from '@sentry/react'
import { Suspense, lazy } from 'react'
import { createBrowserRouter } from 'react-router'
import ActionLoader from '../v2/features/common/components/ui/action-loader.tsx'
import AuthGuard from '../v2/features/auth/components/auth-guard.tsx'
import RoleGuard from '../v2/features/auth/components/role-guard.tsx'
import { RoleType } from '../v2/features/common/types/role-type.ts'
import AdminPage from '../v2/pages/admin-page.tsx'
import ErrorPage from '../v2/pages/error-page.tsx'
import MetabasePage from '../v2/pages/metabase-page.tsx'
import HomePage from '../v2/pages/home-page.tsx'
import InquiryListPage from '../v2/pages/inquiry-list-page.tsx'
import InquiryPage from '../v2/pages/inquiry-page.tsx'
import ManagePage from '../v2/pages/manage-page.tsx'
import MissionListPamPage from '../v2/pages/mission-list-pam-page.tsx'
import MissionListUlamPage from '../v2/pages/mission-list-ulam-page.tsx'
import MissionPamPage from '../v2/pages/mission-pam-page.tsx'
import MissionUlamPage from '../v2/pages/mission-ulam-page.tsx'
import SignupPage from '../v2/pages/signup-page.tsx'

// Lazy-loaded so the DSFR design system (imported only by this page) is split
// into its own async chunk and fetched solely when /login is visited.
const LoginPage = lazy(() => import('../v2/pages/login-page.tsx'))

const sentryCreateBrowserRouter = wrapCreateBrowserRouterV6(createBrowserRouter)

export const router = sentryCreateBrowserRouter([
  // HomePage route
  {
    path: ROOT_PATH,
    element: <HomePage />,
    errorElement: <ErrorPage />
  },
  // Auth routes
  {
    path: LOGIN_PATH,
    element: (
      <Suspense fallback={<ActionLoader />}>
        <LoginPage />
      </Suspense>
    )
  },
  {
    path: SIGNUP_PATH,
    element: <SignupPage />
  },
  // ULAM Mission routes
  {
    path: '/ulam/missions',
    element: (
      <AuthGuard>
        <MissionListUlamPage />
      </AuthGuard>
    ),
    errorElement: <ErrorPage />
  },
  {
    path: `/ulam/missions/:missionId/:actionId?`,
    element: (
      <AuthGuard>
        <MissionUlamPage />
      </AuthGuard>
    ),
    errorElement: <ErrorPage />
  },
  // ULAM Inquiries routes
  {
    path: `/ulam/inquiries?`,
    element: (
      <AuthGuard>
        <InquiryListPage />
      </AuthGuard>
    ),
    errorElement: <ErrorPage />
  },
  {
    path: `/ulam/inquiries/:inquiryId/:actionId?`,
    element: (
      <AuthGuard>
        <InquiryPage />
      </AuthGuard>
    ),
    errorElement: <ErrorPage />
  },
  // PAM Mission routes
  {
    path: '/pam/missions',
    element: (
      <AuthGuard>
        <MissionListPamPage />
      </AuthGuard>
    ),
    errorElement: <ErrorPage />
  },
  {
    path: `/pam/missions/:missionId/:actionId?`,
    element: (
      <AuthGuard>
        <MissionPamPage />
      </AuthGuard>
    ),
    errorElement: <ErrorPage />
  },
  // Metabase route
  {
    path: '/metabase',
    element: (
      <AuthGuard>
        <RoleGuard roles={[RoleType.ADMIN]}>
          <MetabasePage />
        </RoleGuard>
      </AuthGuard>
    ),
    errorElement: <ErrorPage />
  },
  // Admin routes
  {
    path: '/admin',
    element: (
      <AuthGuard>
        <RoleGuard roles={[RoleType.ADMIN]}>
          <AdminPage />
        </RoleGuard>
      </AuthGuard>
    ),
    errorElement: <ErrorPage />
  },
  {
    path: '/manage',
    element: (
      <AuthGuard>
        <RoleGuard roles={[RoleType.MANAGER_PAM, RoleType.MANAGER_ULAM]}>
          <ManagePage />
        </RoleGuard>
      </AuthGuard>
    ),
    errorElement: <ErrorPage />
  }
])
