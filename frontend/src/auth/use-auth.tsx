import { useEffect, useState } from 'react'
import AuthToken from './token'
import { useNavigate } from 'react-router-dom'

const authToken = new AuthToken()

const useAuth = (): { isAuthenticated: boolean; logout: () => void } => {
  const [isAuthenticated, setIsAuthenticated] = useState<boolean>(!!authToken.get())
  const navigate = useNavigate()

  const logout = (): void => {
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
