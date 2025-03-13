import AuthToken from '@features/auth/utils/token'
import { useQueryClient } from '@tanstack/react-query'
import { jwtDecode, JwtPayload } from 'jwt-decode'
import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { QueryKeyType } from '../../common/types/query-key-type'

type AuthHook = {
  isAuthenticated: boolean
  logout: () => Promise<void>
  isLoggedIn: () => Token | undefined
  navigateAndResetCache: (to: string, keys: QueryKeyType[]) => Promise<void>
}
export type Token = JwtPayload & { userId: number }
const authToken = new AuthToken()

const useAuth = (): AuthHook => {
  const navigate = useNavigate()
  const queryClient = useQueryClient()
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(!!authToken.get())

  const logout = async (): Promise<void> => {
    authToken.remove()
    setIsAuthenticated(false)
    navigate('/login', { replace: true })
  }

  const navigateAndResetCache = async (to: string, keys: QueryKeyType[] = []) => {
    keys.forEach(key => queryClient.invalidateQueries({ queryKey: [key.toString()] }))

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
