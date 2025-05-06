// inspired from https://tkdodo.eu/blog/effective-react-query-keys

export const missionsKeys = {
  all: () => ['missions'] as const,
  filter: (filters: string) => [...missionsKeys.all(), { filters }] as const,
  byId: (id: string) => [...missionsKeys.all(), `${id}`] as const
}

export const actionsKeys = {
  all: () => ['actions'] as const,
  filter: (filters: string) => [...actionsKeys.all(), { filters }] as const,
  byId: (id: string) => [...actionsKeys.all(), `${id}`] as const
}

export const natinfsKeys = {
  all: () => ['natinfs'] as const
}

export const administrationKeys = {
  all: () => ['administrations'] as const
}

export const agentRolesKeys = {
  all: () => ['agentRoles'] as const
}

export const agentServicesKeys = {
  all: () => ['agentServices'] as const
}

export const agentsKeys = {
  all: () => ['agents'] as const
}

export const controlUnitResourcesKeys = {
  all: () => ['controlUnitResources'] as const
}

export const usersKeys = {
  all: () => ['users'] as const,
  byId: (id: number) => [...usersKeys.all(), `${id}`] as const
}
