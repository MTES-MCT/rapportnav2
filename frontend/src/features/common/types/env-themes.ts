export type EnvTheme = {
  id: number
  name: string
  startedAt?: string
  endedAt?: string
  subThemes: EnvTheme[]
}
