import React from 'react'
import useGetAgentRoles from '../../../common/services/use-agent-roles'
import useAdminCreateOrUpdateAgentRoleMutation from '../../services/use-admin-create-or-update-agents-role-service'
import { AgentRole } from '../../types/admin-agent-types'
import { AdminAction } from '../../types/admin-services-type'
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

type AdminAgentProps = {}

const AdminAgentRoleItem: React.FC<AdminAgentProps> = () => {
  const { data: agentRoles } = useGetAgentRoles()
  const createOrUpdateMutation = useAdminCreateOrUpdateAgentRoleMutation()

  const handleSubmit = async (action: AdminAction, value: AgentRole) => {
    if (action !== 'DELETE') await createOrUpdateMutation.mutateAsync(value)
  }

  return (
    <AdminBasicItemGeneric
      cells={CELLS}
      module="Agent Roles"
      data={agentRoles}
      onSubmit={handleSubmit}
      form={AdminAgentRoleForm}
      mainButtonLabel={`Créer un rôle`}
    />
  )
}

export default AdminAgentRoleItem
