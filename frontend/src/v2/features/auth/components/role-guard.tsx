import { Suspense } from 'react'
import { Navigate } from 'react-router-dom'
import { RoleType } from '../../common/types/role-type.ts'
import useAuth from '../hooks/use-auth.tsx'

type RoleGuardProps = {
  roles: RoleType[]
  children: JSX.Element
}

export default function RoleGuard(props: RoleGuardProps): JSX.Element | null {
  const { hasRoles } = useAuth()
  const allow = () => {
    return <Suspense>{props.children}</Suspense>
  }
  const denied = () => {
    return <Navigate to={`/home`} replace />
  }
  return !hasRoles(props.roles) ? denied() : allow()
}
