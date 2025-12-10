import { Icon, THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
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
  const deleteRole = useAdminDeleteAgentRoleMutation()
  const createOrUpdateMutation = useAdminCreateOrUpdateAgentRoleMutation()

  const handleSubmit = async (action: AdminActionType, value: AdminAgentRole) => {
    if (action === AdminActionType.DELETE) await deleteRole.mutateAsync(value.id)
    if (action !== AdminActionType.DELETE) await createOrUpdateMutation.mutateAsync(value)
  }

  return (
    <AdminBasicItemGeneric
      cells={CELLS}
      actions={ACTIONS}
      data={agentRoles}
      title="Agent Roles"
      onSubmit={handleSubmit}
    />
  )
}

export default AdminAgentRoleItem
