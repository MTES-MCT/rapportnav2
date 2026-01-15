import { LOGIN_PATH, ROOT_PATH, SIGNUP_PATH } from '@router/routes.tsx'
import { wrapCreateBrowserRouterV6 } from '@sentry/react'
import { createBrowserRouter } from 'react-router-dom'
import ErrorPage from '../pages/error-page.tsx'
import Login from '../pages/login.tsx'
import SignUp from '../pages/signup.tsx'
import AdminGuard from '../v2/features/auth/components/admin-guard.tsx'
import AuthGuard from '../v2/features/auth/components/auth-guard.tsx'
import AdminPage from '../v2/pages/admin-page.tsx'
import Home2 from '../v2/pages/home.tsx'
import InquiryListPage from '../v2/pages/inquiry-list-page.tsx'
import InquiryPage from '../v2/pages/inquiry-page.tsx'
import MissionListPamPage from '../v2/pages/mission-list-pam-page.tsx'
import MissionListUlamPage from '../v2/pages/mission-list-ulam-page.tsx'
import MissionPamPage from '../v2/pages/mission-pam-page.tsx'
import MissionUlamPage from '../v2/pages/mission-ulam-page.tsx'

const sentryCreateBrowserRouter = wrapCreateBrowserRouterV6(createBrowserRouter)

export const router = sentryCreateBrowserRouter([
  {
    path: ROOT_PATH,
    element: <Home2 />,
    errorElement: <ErrorPage />
  },
  {
    path: LOGIN_PATH,
    element: <Login />
  },
  {
    path: SIGNUP_PATH,
    element: <SignUp />
  },
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
  {
    path: `${'/ulam/inquiries'}?`,
    element: (
      <AuthGuard>
        <InquiryListPage />
      </AuthGuard>
    ),
    errorElement: <ErrorPage />
  },
  {
    path: `${'/ulam/inquiries'}/:inquiryId/:actionId?`,
    element: (
      <AuthGuard>
        <InquiryPage />
      </AuthGuard>
    ),
    errorElement: <ErrorPage />
  },

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
    path: `/pam/missions`,
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
