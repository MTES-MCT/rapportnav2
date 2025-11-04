import Text from '@common/components/ui/text'
import { Icon, Select, THEME } from '@mtes-mct/monitor-ui'
import { sortBy } from 'lodash'
import React, { useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import useGetAgentRoles from '../../../common/services/use-agent-roles'
import useGetAdminCrewServices from '../../services/use-admin-agent-crew-service'
import useGetAdminAgentServices from '../../services/use-admin-agents-service'
import useAdminCreateOrUpdateCrewMutation from '../../services/use-admin-create-update-agent-crew-service'
import useAdminServiceListQuery from '../../services/use-admin-services-service'
import { AdminAction, AdminActionType } from '../../types/admin-action'
import { AdminAgentServiceInput, AdminServiceWithAgent } from '../../types/admin-agent-types'
import { AdminService } from '../../types/admin-services-type'
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
  const { data: roles } = useGetAgentRoles()
  const { data: agents } = useGetAdminAgentServices()
  const { data: services } = useAdminServiceListQuery()
  const { data: agentCrews } = useGetAdminCrewServices()
  const createOrUpdateMutation = useAdminCreateOrUpdateCrewMutation()

  const [currentService, setCurrentService] = useState<AdminService>()
  const [currentCrew, setCurrentCrew] = useState<AdminServiceWithAgent>()
  const handleSubmit = async (action: AdminActionType, value: AdminAgentServiceInput) => {
    if (!value.serviceId) value = { ...value, serviceId: currentService?.id!! }
    if (action !== AdminActionType.DELETE) createOrUpdateMutation.mutateAsync(value)
  }

  useEffect(() => {
    setCurrentCrew(agentCrews?.find(c => c.service.id === currentService?.id))
  }, [currentService, agentCrews])

  return (
    <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%', height: '100%' }}>
      <Stack.Item style={{ width: '30%' }}>
        <Text as="h1">{'Crews'}</Text>
      </Stack.Item>
      <Stack.Item style={{ width: '30%' }}>
        <Select
          name="crew"
          value={currentService?.id}
          label="Selectionner votre équipe"
          options={services?.map(e => ({ value: e.id, label: e.name })) ?? []}
          onChange={nextValue => setCurrentService(services?.find(c => c.id === nextValue))}
        />
      </Stack.Item>

      <Stack.Item style={{ width: '100%', overflowY: 'scroll' }}>
        {currentService && (
          <AdminBasicItemGeneric
            cells={CELLS}
            onSubmit={handleSubmit}
            title={currentService?.name ?? ''}
            data={sortBy(
              currentCrew?.agentServices?.map(agentService => ({
                id: agentService.id,
                agentId: agentService.agent.id,
                roleId: agentService.role?.id,
                serviceId: currentService.id,
                name: `${agentService.agent?.firstName} ${agentService.agent?.lastName}`,
                role: `${agentService.role?.id} - ${agentService.role?.title}`,
                createdAt: agentService.createdAt,
                updatedAt: agentService.updatedAt,
                disabledAt: agentService.disabledAt
              })),
              ['updatedAt']
            )}
            actions={ACTIONS.map(action => ({
              ...action,
              formProps: {
                agents,
                roles,
                disabledAgents: currentCrew?.agentServices?.map(a => a.agent.id?.toString()) ?? []
              }
            }))}
          />
        )}
      </Stack.Item>
    </Stack>
  )
}

export default AdminAgentServiceItem
