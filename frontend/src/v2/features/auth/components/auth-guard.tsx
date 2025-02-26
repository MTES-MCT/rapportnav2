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

  const Denied = () => {
    return <Navigate to={`/login`} replace />
  }

  const Allow = () => {
    const { data: user } = useGetUserQuery(isLoggedIn()?.userId)
    setUser(user)
    if (!user) return
    return <Suspense>{props.children}</Suspense>
  }
  return !isAuthenticated ? <Denied /> : <Allow />
}
