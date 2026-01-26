import Text from '@common/components/ui/text'
import { AgentRole } from '@common/types/crew-types'
import { Icon, TextInput, THEME } from '@mtes-mct/monitor-ui'
import { orderBy } from 'lodash'
import React, { useMemo, useState } from 'react'
import { Stack } from 'rsuite'
import useGetAgentRoles from '../../../common/services/use-agent-roles'
import useAdminCreateOrUpdateAgentRoleMutation from '../../services/use-admin-create-update-agents-role-service'
import useAdminDeleteAgentRoleMutation from '../../services/use-admin-delete-roles-service'
import { AdminAction, AdminActionType } from '../../types/admin-action'
import { AdminAgentRole } from '../../types/admin-agent-types'
import AdminAgentRoleForm from '../ui/admin-agent-role'
import AdminBasicItemGeneric from './admin-basic-item-generic'

const CELLS = [
  { key: 'id', label: 'Id', width: 60 },
  { key: 'title', label: 'Titre', width: 240 },
  { key: 'priority', label: 'Priorité', width: 60 },
  { key: 'createdAt', label: 'Date de Creation', width: 200 },
  { key: 'updatedAt', label: 'Dernière mise à jour', width: 200 },
  { key: 'deletedAt', label: 'Date de suppression', width: 200 }
]

const ACTIONS: AdminAction[] = [
  {
    isMain: true,
    label: 'Créer un rôle',
    key: AdminActionType.CREATE,
    form: AdminAgentRoleForm
  },
  {
    label: `Mise à jour d'un rôle`,
    key: AdminActionType.UPDATE,
    form: AdminAgentRoleForm,
    icon: Icon.EditUnbordered
  },
  {
    label: `Supprimer un rôle`,
    color: THEME.color.maximumRed,
    key: AdminActionType.DELETE,
    form: () => <>Voulez-vous vraiment supprimer ce rôle?</>,
    icon: Icon.Delete
  }
]

type AdminAgentProps = {}

const AdminAgentRoleItem: React.FC<AdminAgentProps> = () => {
  const { data: agentRoles } = useGetAgentRoles()
  const [search, setSearch] = useState<string>()
  const deleteRole = useAdminDeleteAgentRoleMutation()
  const createOrUpdateMutation = useAdminCreateOrUpdateAgentRoleMutation()

  const handleSubmit = async (action: AdminActionType, value: AdminAgentRole) => {
    if (action === AdminActionType.DELETE) await deleteRole.mutateAsync(value.id)
    if (action !== AdminActionType.DELETE) await createOrUpdateMutation.mutateAsync(value)
  }

  const filterRoles = (searchValue: string, values?: AgentRole[]) =>
    values?.filter(service => {
      const query = searchValue.toLowerCase().trim()
      const name = service.title?.toLowerCase() ?? ''
      return name.includes(query)
    })

  const filteredRoles = useMemo(() => {
    return orderBy(
      search?.trim() ? filterRoles(search, agentRoles) : agentRoles,
      [obj => new Date(obj.updatedAt ?? 0)],
      ['desc']
    )
  }, [agentRoles, search])

  return (
    <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%', height: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem" alignItems="flex-end" justifyContent="flex-start">
          <Stack.Item>
            <Text as="h1" size={30}>{`Rôles`}</Text>
          </Stack.Item>
          <Stack.Item style={{ width: '30%' }}>
            <TextInput name="search" value={search} label="Rechercher" onChange={nextValue => setSearch(nextValue)} />
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <AdminBasicItemGeneric
          cells={CELLS}
          actions={ACTIONS}
          data={filteredRoles}
          onSubmit={handleSubmit}
          title={search?.trim() ? 'Filtrés' : 'Tout'}
        />
      </Stack.Item>
    </Stack>
  )
}

export default AdminAgentRoleItem
