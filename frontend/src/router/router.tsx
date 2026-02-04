import { LOGIN_PATH, ROOT_PATH, SIGNUP_PATH } from '@router/routes.tsx'
import { wrapCreateBrowserRouterV6 } from '@sentry/react'
import { createBrowserRouter } from 'react-router-dom'
import ErrorPage from '../v2/pages/error-page.tsx'
import LoginPage from '../v2/pages/login-page.tsx'
import SignupPage from '../v2/pages/signup-page.tsx'
import AdminGuard from '../v2/features/auth/components/admin-guard.tsx'
import AuthGuard from '../v2/features/auth/components/auth-guard.tsx'
import AdminPage from '../v2/pages/admin-page.tsx'
import HomePage from '../v2/pages/home-page.tsx'
import InquiryListPage from '../v2/pages/inquiry-list-page.tsx'
import InquiryPage from '../v2/pages/inquiry-page.tsx'
import MissionListPamPage from '../v2/pages/mission-list-pam-page.tsx'
import MissionListUlamPage from '../v2/pages/mission-list-ulam-page.tsx'
import MissionPamPage from '../v2/pages/mission-pam-page.tsx'
import MissionUlamPage from '../v2/pages/mission-ulam-page.tsx'

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
    element: <LoginPage />
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
  // Admin routes
  {
    path: '/admin',
    element: (
      <AuthGuard>
        <AdminGuard>
          <AdminPage />
        </AdminGuard>
      </AuthGuard>
    ),
    errorElement: <ErrorPage />
  }
])
