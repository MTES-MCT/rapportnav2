// inspired from https://tkdodo.eu/blog/effective-react-query-keys

export const missionKeys = {
  all: () => ['missions'] as const,
  filter: (filters: string) => [...missionKeys.all(), { filters }] as const,
  detail: (id: number) => [...missionKeys.all(), `${id}`] as const
}

export const actionKeys = {
  all: () => ['actions'] as const,
  filter: (filters: string) => [...actionKeys.all(), { filters }] as const,
  detail: (id: string) => [...actionKeys.all(), `${id}`] as const
}
