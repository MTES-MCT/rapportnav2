import Text from '@common/components/ui/text.tsx'
import { formatDateForFrenchHumans } from '@common/utils/dates-for-humans.ts'
import { DataTable } from '@mtes-mct/monitor-ui'
import { FC } from 'react'
import { Container, Panel, Stack } from 'rsuite'
import useGetAgentServices from '../features/common/services/use-agent-services.tsx'
import { AgentService, ServiceWithAgents } from '../features/common/types/service-agents-types.ts'

const AdminCrewPage: FC = () => {
  const { data: agentServices } = useGetAgentServices()

  if (agentServices) {
    return (
      <Container>
        <Text as={'h1'}>Gestion des équipages</Text>

        <Stack direction={'row'} alignItems={'flex-start'} wrap={true} style={{ marginTop: '2rem' }}>
          {agentServices.map((serviceData: ServiceWithAgents) => (
            <Stack.Item style={{ width: '50%' }}>
              <Panel header={`Service #${serviceData.service.id} - ${serviceData.service.name}`} bordered>
                <DataTable
                  columns={[
                    { accessorKey: 'id' },
                    { accessorKey: 'name' },
                    { accessorKey: 'role' },
                    { accessorKey: 'disabledAt' }
                  ]}
                  data={serviceData.agents.map((agent: AgentService) => ({
                    id: agent.agent.id,
                    name: `${agent.agent?.firstName} ${agent.agent?.lastName}`,
                    role: `${agent.role?.id} - ${agent.role?.title}`,
                    disabledAt: formatDateForFrenchHumans(agent.disabledAt)
                  }))}
                  initialSorting={[{ id: 'id', desc: false }]}
                />
              </Panel>
            </Stack.Item>
          ))}
        </Stack>
      </Container>
    )
  }

  return null
}

export default AdminCrewPage
