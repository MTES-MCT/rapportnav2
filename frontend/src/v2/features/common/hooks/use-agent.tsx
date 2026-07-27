import useAgentsQuery from '../services/use-agents'
import { Agent } from '../types/crew-type.ts'

interface AgentHook {
  getAgentById: (id?: number) => string
  agents: { label: string; value: number }[]
  getAgent: (id?: number) => Agent | undefined
}

export function useAgent(): AgentHook {
  const { data: agents } = useAgentsQuery()

  const getAgentById = (id?: number) => {
    if (!id) return 'Inconnu'
    const agent = agents?.find(agent => agent.id === id)
    return `${agent?.firstName} ${agent?.lastName}`
  }
  const getAgent = (id?: number) => agents?.find(agent => agent.id === id)

  const getAgents = () =>
    agents?.map(agent => ({ value: agent.id, label: `${agent?.firstName} ${agent?.lastName}` })) ?? []

  return { getAgent, getAgentById, agents: getAgents() }
}
