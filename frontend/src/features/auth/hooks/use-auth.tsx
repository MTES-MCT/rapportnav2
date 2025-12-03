import { useEffect, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import AuthToken from '../utils/token.ts'

type AuthHook = {
  isAuthenticated: boolean
  logout: () => Promise<void>
}
const authToken = new AuthToken()

const useAuth = (): AuthHook => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(!!authToken.get())
  const navigate = useNavigate()

  const logout = async (): Promise<void> => {
    // Remove the token from localStorage
    authToken.remove()
    // Update the state to reflect that the user is not authenticated
    setIsAuthenticated(false)
    // Reset history to /login
    navigate('/login', { replace: true })
  }

  useEffect(() => {
    if (!!authToken.get() && !isAuthenticated) {
      setIsAuthenticated(true)
    }
  }, [isAuthenticated])

  return { isAuthenticated, logout }
}

export default useAuth
