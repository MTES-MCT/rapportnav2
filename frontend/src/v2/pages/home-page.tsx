import { LOGIN_PATH } from '@router/routes.tsx'
import { useGlobalRoutes } from '@router/use-global-routes'
import { useIsRestoring } from '@tanstack/react-query'
import { FC, useEffect } from 'react'
import { useNavigate } from 'react-router'
import { useGetCurrentUserQuery } from '../features/common/services/use-user'
import { setUser } from '../store/slices/user-reducer'

const HomePage: FC = () => {
  const navigate = useNavigate()
  // Resolve the current user from `users/me`. This is the only path that recognises a
  // ProConnect session user (no local JWT); populating the store lets useAuth /
  // useGlobalRoutes compute the right landing page. For JWT users it just refreshes the
  // same identity.
  const { data: user, isLoading, isError } = useGetCurrentUserQuery()
  // See AuthGuard: the persisted React Query cache restores asynchronously, during which the
  // query is paused (isLoading false, user undefined). We must not redirect during that window.
  const isRestoring = useIsRestoring()
  const { homeUrl } = useGlobalRoutes()

  useEffect(() => {
    if (user) setUser(user)
  }, [user])

  useEffect(() => {
    // Wait for the who-am-I call to settle (and for the persisted cache to finish restoring)
    // before redirecting, otherwise a session user would be bounced to /login before their
    // identity is known — the "sign in twice" glitch.
    if (isLoading || isRestoring) return

    if (isError || !user) {
      navigate(LOGIN_PATH, { replace: true })
      return
    }

    // Once the user is known, homeUrl resolves to the module landing page (never /login).
    if (homeUrl && homeUrl !== LOGIN_PATH) {
      navigate(homeUrl, { replace: true })
    }
  }, [isLoading, isRestoring, isError, user, homeUrl, navigate])

  return <div />
}

export default HomePage
