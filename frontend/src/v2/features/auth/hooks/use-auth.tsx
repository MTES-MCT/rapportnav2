import { useApolloClient } from '@apollo/client'
import AuthToken from '@features/auth/utils/token'
import { jwtDecode, JwtPayload } from 'jwt-decode'
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'

type AuthHook = {
  isAuthenticated: boolean
  logout: () => Promise<void>
  isLoggedIn: () => Token | undefined
  navigateAndResetCache: (to: string) => Promise<void>
}
export type Token = JwtPayload & { userId: number }
const authToken = new AuthToken()

const useAuth = (): AuthHook => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(!!authToken.get())
  const navigate = useNavigate()
  const apolloClient = useApolloClient()

  const logout = async (): Promise<void> => {
    // Remove the token from localStorage
    authToken.remove()
    // Update the state to reflect that the user is not authenticated
    setIsAuthenticated(false)
    // TODO centralise the following two lines into a class - also used elsewhere
    // reset apollo store
    await apolloClient.clearStore()
    // flush apollo persist cache
    apolloClient.cache.evict({})
    // Reset history to /login
    navigate('/login', { replace: true })
  }

  const navigateAndResetCache = async (to: string) => {
    // TODO centralise the following into a class - also used in use-auth()
    // reset apollo store
    await apolloClient.clearStore()
    // flush apollo persist cache
    apolloClient.cache.evict({})
    navigate(to)
  }

  const isLoggedIn = (): Token | undefined => {
    const token = authToken?.get()
    return token ? jwtDecode(token) : undefined
  }

  useEffect(() => {
    if (!!authToken.get() && !isAuthenticated) {
      setIsAuthenticated(true)
    }
  }, [isAuthenticated])

  return { isAuthenticated, logout, navigateAndResetCache, isLoggedIn }
}

export default useAuth
