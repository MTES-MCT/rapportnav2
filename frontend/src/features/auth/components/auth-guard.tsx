import { Suspense } from 'react'
import { Navigate } from 'react-router-dom'
import useAuth from '../hooks/use-auth'

type AuthGuardProps = {
  children: JSX.Element
}

export default function AuthGuard(props: AuthGuardProps): JSX.Element | null {
  const { isAuthenticated } = useAuth()

  const denied = () => {
    return <Navigate to={`/login`} replace />
  }

  const allow = () => {
    return <Suspense>{props.children}</Suspense>
  }
  return !isAuthenticated ? denied() : allow()
}
