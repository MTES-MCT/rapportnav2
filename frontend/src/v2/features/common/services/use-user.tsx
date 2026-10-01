import { useQuery } from '@tanstack/react-query'
import axios from '../../../../query-client/axios'
import { User } from '../types/user'
import { usersKeys } from './query-keys.ts'

const useGetUserQuery = (userId?: number) => {
  const fetchUser = (): Promise<User> => axios.get(`users/${userId}`).then(response => response.data)

  const query = useQuery<User>({
    queryKey: usersKeys.byId(userId!!),
    enabled: !!userId,
    queryFn: fetchUser,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: 2
  })
  return query
}

/**
 * Fetches the currently authenticated user from `users/me`, resolved by the backend
 * from the SecurityContext. Works for both auth methods:
 * - JWT/password users: the Bearer header is attached by the axios interceptor.
 * - ProConnect users: the JSESSIONID cookie is sent (withCredentials).
 *
 * A 401/403 (not authenticated) surfaces as a query error and is used by AuthGuard
 * to redirect to the login page.
 */
export const useGetCurrentUserQuery = () => {
  const fetchCurrentUser = (): Promise<User> => axios.get('users/me').then(response => response.data)

  return useQuery<User>({
    queryKey: usersKeys.me(),
    queryFn: fetchCurrentUser,
    refetchIntervalInBackground: false,
    refetchOnWindowFocus: false,
    refetchOnMount: false,
    retry: false
  })
}

export default useGetUserQuery
