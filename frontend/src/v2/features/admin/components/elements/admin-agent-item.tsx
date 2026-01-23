import Text from '@common/components/ui/text'
import { Checkbox, Icon, Select, TextInput, THEME } from '@mtes-mct/monitor-ui'
import { orderBy } from 'lodash'
import React, { useMemo, useState } from 'react'
import { Stack } from 'rsuite'
import useGetAgentRoles from '../../../common/services/use-agent-roles'
import useGetAdminAgentServices from '../../services/use-admin-agents-service'
import useAdminCreateOrUpdateAgentMutation from '../../services/use-admin-create-update-agents-service'
import useAdminCreateOrUpdateUserMutation from '../../services/use-admin-create-update-user-service'
import useAdminDeleteAgentMutation from '../../services/use-admin-delete-agents-service'
import useAdminAgentDisableMutation from '../../services/use-admin-disable-agent-service'
import useAdminMigrateAgentMutation from '../../services/use-admin-migrate-agents-service'
import useAdminServiceListQuery from '../../services/use-admin-services-service'
import { AdminAction, AdminActionType } from '../../types/admin-action'
import { AdminAgent, AdminAgentInput, AdminUserFromAgentInput, AdminUserInput } from '../../types/admin-agent-types'
import { AdminService } from '../../types/admin-services-type'
import AdminAgentForm from '../ui/admin-agent-form'
import AdminUserForm from '../ui/admin-user-form'
import AdminBasicItemGeneric from './admin-basic-item-generic'

const CELLS = [
  { key: 'id', label: 'Id', width: 60 },
  { key: 'name', label: 'Agent name', width: 250 },
  { key: 'role', label: 'Agent role', width: 200 },
  { key: 'service', label: 'Service', width: 200 },
  { key: 'userId', label: 'User id', width: 60 },
  { key: 'createdAt', label: 'Date de Creation', width: 200 },
  { key: 'updatedAt', label: 'Dernière mise à jour', width: 200 },
  { key: 'disabledAt', label: 'Date de désactivation', width: 200 }
]

const ACTIONS: AdminAction[] = [
  {
    isMain: true,
    key: AdminActionType.CREATE,
    form: AdminAgentForm,
    label: `Créer un Agent`
  },
  {
    title: 'Editer',
    form: AdminAgentForm,
    icon: Icon.EditUnbordered,
    key: AdminActionType.UPDATE,
    label: `Mise à jour d'un agent`
  },
  {
    icon: Icon.Reset,
    title: 'change service',
    key: AdminActionType.MIGRATE_AGENT,
    label: `Changer l'agent de service`,
    form: AdminAgentForm
  },
  {
    form: AdminUserForm,
    label: `Create user`,
    title: 'créer un user',
    icon: Icon.GroupPerson,
    key: AdminActionType.CREATE_USER,
    disabled: (rowData: any) => !!(rowData as AdminAgent).userId
  },
  {
    icon: Icon.Archive,
    title: 'Désactiver',
    label: `Désativer cet agent sur ce service?`,
    key: AdminActionType.DISABLE_AGENT,
    form: () => <>Voulez-vous vraiment desactiver cet agent?</>,
    disabled: (rowData: unknown) => !!(rowData as AdminAgent).disabledAt
  },
  {
    icon: Icon.Delete,
    title: 'Supprimer',
    key: AdminActionType.DELETE,
    color: THEME.color.maximumRed,
    label: `Supprimer cet agent sur ce service?`,
    form: () => <>Voulez-vous vraiment supprimer définitivement cet agent?</>
  }
]

type AdminAgentProps = {}

const AdminAgentItem: React.FC<AdminAgentProps> = () => {
  const { data: roles } = useGetAgentRoles()
  const [search, setSearch] = useState<string>()
  const { data: agents } = useGetAdminAgentServices()
  const [onlyActive, setOnlyActive] = useState<boolean>(true)

  const deleteAgent = useAdminDeleteAgentMutation()
  const disableAgent = useAdminAgentDisableMutation()
  const migrateAgent = useAdminMigrateAgentMutation()
  const { data: services } = useAdminServiceListQuery()
  const createUser = useAdminCreateOrUpdateUserMutation()
  const createOrUpdateAgent = useAdminCreateOrUpdateAgentMutation()
  const [currentService, setCurrentService] = useState<AdminService>()

  const handleSubmit = async (action: AdminActionType, value: AdminAgentInput | AdminUserFromAgentInput) => {
    switch (action) {
      case AdminActionType.DELETE:
        deleteAgent.mutateAsync(value.id)
        break
      case AdminActionType.CREATE_USER:
        const { id, ...input } = value
        const user = await createUser.mutateAsync(input as AdminUserInput)
        createOrUpdateAgent.mutateAsync({ ...value, userId: user.id } as AdminAgentInput)
        break
      case AdminActionType.DISABLE_AGENT:
        disableAgent.mutateAsync(value.id!!)
        break
      case AdminActionType.MIGRATE_AGENT:
        migrateAgent.mutateAsync(value as AdminAgentInput)
        break
      default:
        createOrUpdateAgent.mutateAsync(value as AdminAgentInput)
        break
    }
  }

  const filteredAndSortedAgents = useMemo(() => {
    let filtered = currentService ? agents?.filter(a => a.service.id === currentService?.id) : agents

    if (search?.trim()) {
      const query = search.toLowerCase().trim()
      filtered = filtered?.filter(agent => {
        const firstName = agent.firstName?.toLowerCase() ?? ''
        const lastName = agent.lastName?.toLowerCase() ?? ''
        const fullName = `${firstName} ${lastName}`.toLowerCase()

        return firstName.includes(query) || lastName.includes(query) || fullName.includes(query)
      })
    }
    if (onlyActive) filtered = filtered?.filter(a => a.disabledAt === null)

    return orderBy(
      filtered?.map(agent => ({
        ...agent,
        agentId: agent.id,
        roleId: agent.role?.id,
        serviceId: agent.service?.id,
        service: agent.service.name,
        name: `${agent?.firstName} ${agent?.lastName}`,
        role: `${agent.role?.id} - ${agent.role?.title}`
      })),
      [obj => new Date(obj.updatedAt ?? 0)],
      ['desc']
    )
  }, [agents, currentService, search, onlyActive])

  return (
    <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%', height: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem" alignItems="flex-end" justifyContent="flex-start">
          <Stack.Item>
            <Text as="h1" size={30}>{`Agents`}</Text>
          </Stack.Item>
          <Stack.Item style={{ width: '30%' }}>
            <Select
              name="crew"
              searchable={true}
              label="Par équipe"
              value={currentService?.id}
              onChange={nextValue => setCurrentService(services?.find(c => c.id === nextValue))}
              options={orderBy(services?.map(e => ({ value: e.id, label: e.name })) ?? [], 'label', ['asc'])}
            />
          </Stack.Item>
          <Stack.Item style={{ width: '30%' }}>
            <TextInput name="search" value={search} label="Rechercher" onChange={nextValue => setSearch(nextValue)} />
          </Stack.Item>
        </Stack>
        <Stack style={{ marginTop: 12 }}>
          <Checkbox
            name="onlyActive"
            checked={onlyActive}
            label="Uniquemment actifs"
            onChange={() => setOnlyActive(!onlyActive)}
          />
        </Stack>
      </Stack.Item>

      <Stack.Item style={{ width: '100%', overflowY: 'scroll', marginTop: '2rem' }}>
        <AdminBasicItemGeneric
          cells={CELLS}
          onSubmit={handleSubmit}
          title={currentService?.name ?? 'All '}
          data={filteredAndSortedAgents}
          defaultData={{ serviceId: currentService?.id }}
          actions={ACTIONS.map(action => ({
            ...action,
            formProps: {
              agents,
              roles,
              services
            }
          }))}
        />
      </Stack.Item>
    </Stack>
  )
}

export default AdminAgentItem
