import { RoleType } from './role-type'

export type User = {
  id: string
  name: string
  email: string
  token: string
  roles: RoleType[]
}
