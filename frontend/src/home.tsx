import { FC, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import useAuth from './auth/use-auth'
import { useSelector } from 'react-redux'
import { RootState } from './redux/store.ts'

const Home: FC = () => {
  let navigate = useNavigate()
  const isLoggedIn = useSelector((state: RootState) => state.auth.user);

  useEffect(() => {
    if (isLoggedIn) {
      navigate('/pam/missions', { replace: true })
    } else {
      navigate('/login', { replace: true })
    }
  }, [isLoggedIn, navigate])

  return <div />
}

export default Home
