export interface AdminService {
  id: number
  name: string
  createdAt?: Date
  updatedAt?: Date
  deletedAt?: Date
  serviceType: string
  controlUnits: number[]
}
export type AdminCell = { key: string; label: string; width: number }
