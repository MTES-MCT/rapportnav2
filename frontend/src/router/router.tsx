import AuthGuard from '@features/auth/components/auth-guard.tsx'
import MissionListUlamPage from '@pages/mission-list-ulam-page.tsx'
import MissionUlamPage from '@pages/mission-ulam-page.tsx'
import * as Sentry from '@sentry/react'
import { createBrowserRouter } from 'react-router-dom'
import ErrorPage from '../pages/error-page.tsx'
import Home from '../pages/home.tsx'
import Login from '../pages/login.tsx'
import MissionPage from '../pages/mission-page.tsx'
import MissionsPage from '../pages/missions-page.tsx'
import SignUp from '../pages/signup.tsx'

export const getPath = (path: string) => `/${path}`

export const ROOT_PATH = '/'
export const LOGIN_PATH = 'login'
export const SIGNUP_PATH = 'signup'
export const PAM_HOME_PATH = 'pam/missions'
export const ULAM_HOME_PATH = '/ulam/missions'

const sentryCreateBrowserRouter = Sentry.wrapCreateBrowserRouter(createBrowserRouter)

export const router = sentryCreateBrowserRouter([
  {
    path: ROOT_PATH,
    element: <Home />,
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
  {
    path: ULAM_HOME_PATH,
    element: (
      <AuthGuard>
        <MissionListUlamPage />
      </AuthGuard>
    ),
    errorElement: <ErrorPage />
  },
  {
    path: `${ULAM_HOME_PATH}/:missionId/:actionId?`,
    element: (
      <AuthGuard>
        <MissionUlamPage />
      </AuthGuard>
    ),
    errorElement: <ErrorPage />
  }
])
