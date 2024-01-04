import { createBrowserRouter } from 'react-router-dom'
import ErrorPage from '../error-page'
import Login from '../auth/login'
import SignUp from '../auth/signup'
import Home from '../home'
import MissionsPage from '../pam/missions/missions-page'
import MissionPage from '../pam/mission/mission-page'

export const router = createBrowserRouter([
  {
    path: '/',
    element: <Home/>,
    errorElement: <ErrorPage/>
  },
  {
    path: 'login',
    element: <Login/>
  },
  {
    path: 'signup',
    element: <SignUp/>
  },
  {
    path: 'pam/missions',
    element: <MissionsPage/>,
    errorElement: <ErrorPage/>
  },
  {
    path: 'pam/missions/:missionId/:actionId?',
    element: <MissionPage/>
  }
])
