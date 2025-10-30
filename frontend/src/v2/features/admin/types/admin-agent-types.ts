export type Agent = {
  id?: string
  firstName?: string
  lastName?: string
  services?: string[]
  deletedAt?: Date
  createdAt?: Date
  updatedAt?: Date
}

export type AgentRole = {
  id: string
  title: string
}
