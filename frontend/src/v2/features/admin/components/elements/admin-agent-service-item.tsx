import Text from '@common/components/ui/text'
import { Select } from '@mtes-mct/monitor-ui'
import React, { useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import useGetAgentRoles from '../../../common/services/use-agent-roles'
import useAgentsQuery from '../../../common/services/use-agents'
import useAdminCreateOrUpdateAgentServiceMutation from '../../services/use-admin-create-or-update-crew-service'
import useGetAgentServices from '../../services/use-agent-services'
import { AdminAgentServiceInput, AdminServiceWithAgent } from '../../types/admin-agent-types'
import { AdminAction } from '../../types/admin-services-type'
import AdminAgentServiceForm from '../ui/admin-agent-service-form'
import AdminBasicItemGeneric from './admin-basic-item-generic'

const CELLS = [
  { key: 'id', label: 'Id', width: 60 },
  { key: 'name', label: 'Agent name', width: 300 },
  { key: 'role', label: 'Agent role', width: 200 },
  { key: 'createdAt', label: 'Date de Creation', width: 200 },
  { key: 'updatedAt', label: 'Dernière mise à jour', width: 240 },
  { key: 'disabledAt', label: 'Date de désactivation', width: 200 }
]

type AdminAgentOnServiceProps = {}

const AdminAgentServiceItem: React.FC<AdminAgentOnServiceProps> = () => {
  const { data: agents } = useAgentsQuery()
  const { data: roles } = useGetAgentRoles()
  const { data: agentServices } = useGetAgentServices()
  const createOrUpdateMutation = useAdminCreateOrUpdateAgentServiceMutation()

  const [currentCrew, setCurrentCrew] = useState<AdminServiceWithAgent>()
  const handleSubmit = async (action: AdminAction, value: AdminAgentServiceInput) => {
    if (!value.serviceId) value = { ...value, serviceId: currentCrew?.service.id!! }
    if (action !== 'DELETE') createOrUpdateMutation.mutateAsync(value)
  }

  useEffect(() => {
    setCurrentCrew(agentServices?.find(c => c.service.id === currentCrew?.service.id))
  }, [agentServices])

  return (
    <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%', height: '100%' }}>
      <Stack.Item style={{ width: '30%' }}>
        <Text as="h1">{'Crews'}</Text>
      </Stack.Item>
      <Stack.Item style={{ width: '30%' }}>
        <Select
          name="crew"
          label="Selectionner votre équipe"
          options={agentServices?.map(e => ({ value: e.service.id, label: e.service.name })) ?? []}
          value={currentCrew?.service.id}
          onChange={nextValue => setCurrentCrew(agentServices?.find(c => c.service.id === nextValue))}
        />
      </Stack.Item>

      <Stack.Item style={{ width: '100%', overflowY: 'scroll' }}>
        {currentCrew && (
          <AdminBasicItemGeneric
            cells={CELLS}
            form={AdminAgentServiceForm}
            onSubmit={handleSubmit}
            module={currentCrew?.service?.name ?? ''}
            data={currentCrew?.agentServices?.map(agentService => ({
              id: agentService.id,
              agentId: agentService.agent.id,
              roleId: agentService.role?.id,
              serviceId: currentCrew.service.id,
              name: `${agentService.agent?.firstName} ${agentService.agent?.lastName}`,
              role: `${agentService.role?.id} - ${agentService.role?.title}`,
              createdAt: agentService.createdAt,
              updatedAt: agentService.updatedAt,
              disabledAt: agentService.disabledAt
            }))}
            mainButtonLabel={`Ajouter un Agent à l'équipage`}
            formProps={{
              agents,
              roles,
              disabledAgents: currentCrew?.agentServices.map(a => a.agent.id?.toString()) ?? []
            }}
          />
        )}
      </Stack.Item>
    </Stack>
  )
}

export default AdminAgentServiceItem
