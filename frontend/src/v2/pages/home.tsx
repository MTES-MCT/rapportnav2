import { FC, useEffect } from 'react'
import { useNavigate } from 'react-router-dom'
import useAuth from '../features/auth/hooks/use-auth'
import { ModuleType } from '../features/common/types/module-type'
import { RoleType } from '../features/common/types/role-type'
import { PAM_HOME_PATH, ULAM_V2_HOME_PATH } from '@router/router.tsx'

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
      let url = ULAM_V2_HOME_PATH
      // reuse following once both pam and ulam are on v2
      // let url = `/v2/${ROUTES[user?.roles[0] || RoleType.USER_PAM]}/missions`
      if (user?.roles.includes(RoleType.USER_PAM)) {
        url = PAM_HOME_PATH
      }
      navigate(url, { replace: true })
    } else {
      navigate('/login', { replace: true })
    }
  }, [isAuthenticated, isLoggedIn, navigate])

  return <div />
}

export default Home2
