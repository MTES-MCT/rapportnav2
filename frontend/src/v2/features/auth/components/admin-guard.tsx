import { Suspense } from 'react'
import { Navigate } from 'react-router-dom'
import { RoleType } from '../../common/types/role-type.ts'
import useRole from '../hooks/use-auth.tsx'

type AdminGuardProps = {
  children: JSX.Element
}

export default function AdminGuard(props: AdminGuardProps): JSX.Element | null {
  const { isLoggedIn } = useRole()
  const allow = () => {
    return <Suspense>{props.children}</Suspense>
  }
  const denied = () => {
    return <Navigate to={`/home`} replace />
  }
  return !isLoggedIn()?.roles.includes(RoleType.ADMIN) ? denied() : allow()
}
