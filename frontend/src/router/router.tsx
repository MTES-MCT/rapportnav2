import * as Sentry from '@sentry/react'
import { createBrowserRouter } from 'react-router-dom'
import ErrorPage from '../pages/error-page.tsx'
import Login from '../pages/login.tsx'
import MissionPage from '../pages/mission-page.tsx'
import MissionsPage from '../pages/missions-page.tsx'
import SignUp from '../pages/signup.tsx'
import AuthGuard from '../v2/features/auth/components/auth-guard.tsx'
import AdminCrewPage from '../v2/pages/admin-crew-page.tsx'
import Home2 from '../v2/pages/home.tsx'
import MissionListPamPage from '../v2/pages/mission-list-pam-page.tsx'
import MissionListUlamPage from '../v2/pages/mission-list-ulam-page.tsx'
import MissionPamPage from '../v2/pages/mission-pam-page.tsx'
import MissionUlamPage from '../v2/pages/mission-ulam-page.tsx'
import {
  ADMIN_CREW_PATH,
  LOGIN_PATH,
  PAM_HOME_PATH,
  PAM_V2_HOME_PATH,
  ROOT_PATH,
  SIGNUP_PATH,
  ULAM_V2_HOME_PATH
} from '@router/routes.tsx'

const sentryCreateBrowserRouter = Sentry.wrapCreateBrowserRouter(createBrowserRouter)

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
    path: PAM_HOME_PATH,
    element: (
      <AuthGuard>
        <MissionsPage />
      </AuthGuard>
    ),
    errorElement: <ErrorPage />
  },
  {
    path: 'pam/missions/:missionId/:actionId?',
    element: (
      <AuthGuard>
        <MissionPage />
      </AuthGuard>
    )
  },
  //V2
  {
    path: ULAM_V2_HOME_PATH,
    element: (
      <AuthGuard>
        <MissionListUlamPage />
      </AuthGuard>
    ),
    errorElement: <ErrorPage />
  },
  {
    path: `${ULAM_V2_HOME_PATH}/:missionId/:actionId?`,
    element: (
      <AuthGuard>
        <MissionUlamPage />
      </AuthGuard>
    ),
    errorElement: <ErrorPage />
  },
  {
    path: PAM_V2_HOME_PATH,
    element: (
      <AuthGuard>
        <MissionListPamPage />
      </AuthGuard>
    ),
    errorElement: <ErrorPage />
  },
  {
    path: `${PAM_V2_HOME_PATH}/:missionId/:actionId?`,
    element: (
      <AuthGuard>
        <MissionPamPage />
      </AuthGuard>
    ),
    errorElement: <ErrorPage />
  },
  // admin pages :
  {
    path: ADMIN_CREW_PATH,
    element: (
      <AuthGuard>
        <AdminCrewPage />
      </AuthGuard>
    ),
    errorElement: <ErrorPage />
  }
])
