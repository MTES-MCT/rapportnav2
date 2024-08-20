import { createBrowserRouter } from 'react-router-dom'
import ErrorPage from '../error-page'
import Login from '../auth/login'
import SignUp from '../auth/signup'
import Home from '../home'
import MissionsPage from '../pam/missions/missions-page'
import MissionPage from '../pam/mission/mission-page'
import * as Sentry from '@sentry/react'
import AdminPage from '../admin/admin-page.tsx'

export const getPath = (path: string) => `/${path}`

export const ROOT_PATH = '/'
export const LOGIN_PATH = 'login'
export const SIGNUP_PATH = 'signup'
export const PAM_HOME_PATH = 'pam/missions'

export const ADMIN_PATH = 'admin'

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
    element: <MissionsPage />,
    errorElement: <ErrorPage />
  },
  {
    path: 'pam/missions/:missionId/:actionId?',
    element: <MissionPage />
  },
  {
    path: ADMIN_PATH,
    element: <AdminPage />
  }
])
