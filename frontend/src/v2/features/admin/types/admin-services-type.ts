export interface AdminService {
  id: number
  name: string
  createdAt?: Date
  updatedAt?: Date
  deletedAt?: Date
  serviceType: string
  controlUnits: number[]
}
