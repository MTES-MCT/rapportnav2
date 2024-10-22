import { FC, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import useAuth from '../features/auth/hooks/use-auth.tsx'

const Home: FC = () => {
  let navigate = useNavigate()
  const { isAuthenticated } = useAuth()

  useEffect(() => {
    if (isAuthenticated) {
      navigate(`/pam/missions`, { replace: true })
    } else {
      navigate('/login', { replace: true })
    }
  }, [isAuthenticated, navigate])

  return <div />
}

export default Home
