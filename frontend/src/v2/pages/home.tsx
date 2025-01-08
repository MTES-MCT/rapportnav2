import { FC, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import useAuth from '../features/auth/hooks/use-auth'
import { ModuleType } from '../features/common/types/module-type'
import { RoleType } from '../features/common/types/role-type'

const ROUTES = {
  [RoleType.ADMIN]: ModuleType.PAM,
  [RoleType.USER_PAM]: ModuleType.PAM,
  [RoleType.USER_ULAM]: ModuleType.ULAM
}

const Home2: FC = () => {
  let navigate = useNavigate()
  const { isLoggedIn, isAuthenticated } = useAuth()

  useEffect(() => {
    if (isAuthenticated) {
      const user = isLoggedIn()
      navigate(`/v2/${ROUTES[user?.roles[0] || RoleType.USER_PAM]}/missions`, { replace: true })
    } else {
      navigate('/login', { replace: true })
    }
  }, [isAuthenticated, navigate])

  return <div />
}

export default Home2
