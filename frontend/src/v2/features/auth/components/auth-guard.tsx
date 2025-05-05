import { Suspense, useEffect } from 'react'
import { Navigate } from 'react-router-dom'
import { setUser } from '../../../store/slices/user-reducer'
import useGetUserQuery from '../../common/services/use-user'
import useAuth from '../hooks/use-auth'
import { usePrefetchStaticData } from '../../common/services/use-prefetch-static-data.tsx'

type AuthGuardProps = {
  children: JSX.Element
}

export default function AuthGuard(props: AuthGuardProps): JSX.Element | null {
  // check auth status
  const { isLoggedIn, isAuthenticated } = useAuth()

  // get user data
  const { data: user } = useGetUserQuery(isLoggedIn()?.userId)

  // prefill cache with reference data for offline use
  usePrefetchStaticData()

  // set user in store
  useEffect(() => {
    setUser(user)
  }, [user])

  const allow = () => {
    return <Suspense>{props.children}</Suspense>
  }
  const denied = () => {
    return <Navigate to={`/login`} replace />
  }

  return !isAuthenticated ? denied() : allow()
}
