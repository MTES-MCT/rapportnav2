import { createBrowserRouter } from 'react-router-dom'
import ErrorPage from '../pages/error-page.tsx'
import Login from '../pages/login.tsx'
import SignUp from '../pages/signup.tsx'
import Home from '../pages/home.tsx'
import MissionsPage from '../pages/missions-page.tsx'
import MissionPage from '../pages/mission-page.tsx'
import * as Sentry from '@sentry/react'

export const getPath = (path: string) => `/${path}`

export const ROOT_PATH = '/'
export const LOGIN_PATH = 'login'
export const SIGNUP_PATH = 'signup'
export const PAM_HOME_PATH = 'pam/missions'

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
  }
])
