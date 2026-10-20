import { RoleType } from './role-type'

export type User = {
  id: number
  email: string
  firstName?: string
  lastName?: string
  serviceId?: number
  serviceName?: string
  controlUnitId?: number
  roles?: RoleType[]
  disabledAt?: Date
  createdAt?: Date
  updatedAt?: Date
}
