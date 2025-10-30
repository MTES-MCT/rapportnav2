import React from 'react'
import useAgentsQuery from '../../../common/services/use-agents'
import useAdminCreateOrUpdateAgentMutation from '../../services/use-admin-create-or-update-agents-service'
import { Agent } from '../../types/admin-agent-types'
import { AdminAction } from '../../types/admin-services-type'
import AdminAgentForm from '../ui/admin-agent-form'
import AdminBasicItemGeneric from './admin-basic-item-generic'

const CELLS = [
  { key: 'id', label: 'Id', width: 60 },
  { key: 'firstName', label: 'Prénom', width: 240 },
  { key: 'lastName', label: 'Nom', width: 240 },
  { key: 'services', label: 'Service', width: 60 },
  { key: 'createdAt', label: 'Date de Creation', width: 200 },
  { key: 'updatedAt', label: 'Dernière mise à jour', width: 200 },
  { key: 'deletedAt', label: 'Date de supression', width: 200 }
]

type AdminAgentProps = {}

const AdminAgentItem: React.FC<AdminAgentProps> = () => {
  const { data: agents } = useAgentsQuery()
  const createOrUpdateMutation = useAdminCreateOrUpdateAgentMutation()
  const handleSubmit = async (action: AdminAction, value: Agent) => {
    if (action !== 'DELETE') await createOrUpdateMutation.mutateAsync(value)
  }

  return (
    <AdminBasicItemGeneric cells={CELLS} module="Agents" data={agents} onSubmit={handleSubmit} form={AdminAgentForm} />
  )
}

export default AdminAgentItem
