import { useApolloClient } from '@apollo/client'
import { User } from '@features/v2/common/types/user.ts'
import { jwtDecode } from 'jwt-decode'
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import AuthToken from '../utils/token.ts'

type AuthHook = {
  isAuthenticated: boolean
  logout: () => Promise<void>
  isLoggedIn: () => User | undefined
  navigateAndResetCache: (to: string) => Promise<void>
}
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

  const isLoggedIn = (): User | undefined => {
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
