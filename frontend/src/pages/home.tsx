import { ModuleType } from '@common/types/module-type.ts'
import { RoleType } from '@common/utils/role-type.ts'
import { FC, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import useAuth from '../features/auth/hooks/use-auth.tsx'

const ROUTES = {
  [RoleType.ADMIN]: ModuleType.PAM,
  [RoleType.USER_PAM]: ModuleType.PAM,
  [RoleType.USER_ULAM]: ModuleType.ULAM
}

const Home: FC = () => {
  let navigate = useNavigate()
  const { isLoggedIn, isAuthenticated } = useAuth()

  useEffect(() => {
    if (isAuthenticated) {
      const user = isLoggedIn()
      navigate(`/${ROUTES[user?.roles[0] || RoleType.USER_PAM]}/missions`, { replace: true })
    } else {
      navigate('/login', { replace: true })
    }
  }, [isAuthenticated, navigate])

  return <div />
}

export default Home
