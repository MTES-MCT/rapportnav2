import useAgentsQuery from '../services/use-agents'

interface AgentHook {
  getAgentById: (id?: number) => string
  agents: { label: string; value: number }[]
}

export function useAgent(): AgentHook {
  const { data: agents } = useAgentsQuery()

  const getAgentById = (id?: number) => {
    if (!id) return 'Inconnu'
    const agent = agents?.find(agent => agent.id === id)
    return `${agent?.firstName} ${agent?.lastName}`
  }
  const getAgents = () =>
    agents?.map(agent => ({ value: agent.id, label: `${agent?.firstName} ${agent?.lastName}` })) ?? []
  return { getAgentById, agents: getAgents() }
}
