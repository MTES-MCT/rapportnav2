export type EnvTag = {
  id: number
  name: string
  startedAt?: string
  endedAt?: string
  subTags: EnvTag[]
}
