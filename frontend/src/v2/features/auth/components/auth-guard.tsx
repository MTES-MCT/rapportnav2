import { Suspense } from 'react'
import { Navigate } from 'react-router-dom'
import { setUser } from '../../../store/slices/user-reducer'
import useGetUserQuery from '../../common/services/use-user'
import useAuth from '../hooks/use-auth'

type AuthGuardProps = {
  children: JSX.Element
}

export default function AuthGuard(props: AuthGuardProps): JSX.Element | null {
  const { isLoggedIn, isAuthenticated } = useAuth()
  const { data: user } = useGetUserQuery(isLoggedIn()?.userId)
  const denied = () => {
    return <Navigate to={`/login`} replace />
  }

  const allow = () => {
    setUser(user)
    return <Suspense>{props.children}</Suspense>
  }
  return !isAuthenticated ? denied() : allow()
}
