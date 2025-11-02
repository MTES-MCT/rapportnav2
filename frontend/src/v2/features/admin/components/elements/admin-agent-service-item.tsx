import Text from '@common/components/ui/text'
import { Icon, Select, THEME } from '@mtes-mct/monitor-ui'
import React, { useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import useGetAgentRoles from '../../../common/services/use-agent-roles'
import useAgentsQuery from '../../../common/services/use-agents'
import useAdminCreateOrUpdateAgentServiceMutation from '../../services/use-admin-create-or-update-crew-service'
import useGetAdminAgentServices from '../../services/use-agent-services'
import { AdminAction, AdminActionType } from '../../types/admin-action'
import { AdminAgentServiceInput, AdminServiceWithAgent } from '../../types/admin-agent-types'
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

const ACTIONS: AdminAction[] = [
  {
    isMain: true,
    label: `Ajouter un Agent à l'équipage`,
    key: AdminActionType.CREATE,
    form: AdminAgentServiceForm
  },
  {
    label: `Mise à jour d'un agent sur le service`,
    key: AdminActionType.UPDATE,
    form: AdminAgentServiceForm,
    icon: Icon.EditUnbordered
  },
  {
    disabled: true,
    label: `Supprimer cet agent sur ce service?`,
    color: THEME.color.maximumRed,
    key: AdminActionType.DELETE,
    form: () => <>Voulez-vous vraiment desactiver cet agent sur ce Service?</>,
    icon: Icon.Delete
  }
]

type AdminAgentOnServiceProps = {}

const AdminAgentServiceItem: React.FC<AdminAgentOnServiceProps> = () => {
  const { data: agents } = useAgentsQuery()
  const { data: roles } = useGetAgentRoles()
  const { data: agentServices } = useGetAdminAgentServices()
  const createOrUpdateMutation = useAdminCreateOrUpdateAgentServiceMutation()

  const [currentCrew, setCurrentCrew] = useState<AdminServiceWithAgent>()
  const handleSubmit = async (action: AdminActionType, value: AdminAgentServiceInput) => {
    if (!value.serviceId) value = { ...value, serviceId: currentCrew?.service.id!! }
    if (action !== AdminActionType.DELETE) createOrUpdateMutation.mutateAsync(value)
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
            onSubmit={handleSubmit}
            title={currentCrew?.service?.name ?? ''}
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
            actions={ACTIONS.map(action => ({
              ...action,
              formProps: {
                agents,
                roles,
                disabledAgents: currentCrew?.agentServices.map(a => a.agent.id?.toString()) ?? []
              }
            }))}
          />
        )}
      </Stack.Item>
    </Stack>
  )
}

export default AdminAgentServiceItem
