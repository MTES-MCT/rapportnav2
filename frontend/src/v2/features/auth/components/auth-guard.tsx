import { Suspense, useEffect } from 'react'
import { Navigate } from 'react-router'
import * as Sentry from '@sentry/react'
import { useIsRestoring } from '@tanstack/react-query'
import { setUser } from '../../../store/slices/user-reducer'
import { useGetCurrentUserQuery } from '../../common/services/use-user'
import { usePrefetchStaticData } from '../../common/services/use-prefetch-static-data.tsx'

type AuthGuardProps = {
  children: JSX.Element
}

export default function AuthGuard(props: AuthGuardProps): JSX.Element | null {
  // Resolve the current user from `users/me`. This is the single source of truth for
  // both auth methods: JWT/password (Bearer header) and ProConnect (session cookie).
  const { data: user, isLoading, isError } = useGetCurrentUserQuery()
  // While the persisted React Query cache is being restored from localStorage, the query is
  // PAUSED: `isLoading` is false but `user` is still undefined. Without this flag we would wrongly
  // treat that window as "not authenticated" and bounce to /login on the first load after login.
  const isRestoring = useIsRestoring()
  const isAuthenticated = !!user && !isError

  // prefill cache with reference data for offline use
  usePrefetchStaticData()

  // set user in store
  useEffect(() => {
    setUser(user)
  }, [user])

  // Set user context in Sentry for error correlation
  // GDPR: Only pseudonymized ID is sent, no PII (email, name)
  useEffect(() => {
    if (isAuthenticated && user?.id) {
      Sentry.setUser({ id: user.id.toString() })
      if (user.serviceId) {
        Sentry.setTag('service_id', user.serviceId.toString())
      }
    } else {
      Sentry.setUser(null)
      Sentry.setTag('service_id', undefined)
    }
  }, [isAuthenticated, user?.id, user?.serviceId])

  const allow = () => {
    return <Suspense>{props.children}</Suspense>
  }
  const denied = () => {
    return <Navigate to={`/login`} replace />
  }

  // Wait for the "who am I" call to settle before deciding, otherwise a ProConnect
  // session user (no local token) would be bounced to /login on first render. This must also
  // wait while the persisted cache is restoring (isRestoring), during which the query is paused
  // and isLoading is false even though the user is not yet known.
  if (isLoading || isRestoring) {
    return null
  }

  return !isAuthenticated ? denied() : allow()
}
