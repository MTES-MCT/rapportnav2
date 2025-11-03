export interface AdminService {
  id: number
  name: string
  createdAt?: Date
  updatedAt?: Date
  controlUnitId?: number
}
export type AdminCell = { key: string; label: string; width: number }
