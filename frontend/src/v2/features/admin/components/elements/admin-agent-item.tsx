import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { sortBy } from 'lodash'
import React from 'react'
import useAgentsQuery from '../../../common/services/use-agents'
import useAdminCreateOrUpdateAgentMutation from '../../services/use-admin-create-update-agents-service'
import { AdminAction, AdminActionType } from '../../types/admin-action'
import { Agent } from '../../types/admin-agent-types'
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

const ACTIONS: AdminAction[] = [
  {
    isMain: true,
    label: `Ajouter un agent`,
    key: AdminActionType.CREATE,
    form: AdminAgentForm
  },
  {
    label: `Mise à jour d'un agent`,
    key: AdminActionType.UPDATE,
    form: AdminAgentForm,
    icon: Icon.EditUnbordered
  },
  {
    disabled: true,
    label: `Supprimer un agent`,
    color: THEME.color.maximumRed,
    key: AdminActionType.DELETE,
    form: () => <>{`Voulez-vous vraiment supprimer cet agent?`}</>,
    icon: Icon.Delete
  }
]

type AdminAgentProps = {}

const AdminAgentItem: React.FC<AdminAgentProps> = () => {
  const { data: agents } = useAgentsQuery()
  const createOrUpdateMutation = useAdminCreateOrUpdateAgentMutation()
  const handleSubmit = async (action: AdminActionType, value: Agent) => {
    if (action !== 'DELETE') await createOrUpdateMutation.mutateAsync(value)
  }

  return (
    <AdminBasicItemGeneric
      cells={CELLS}
      title="Agents"
      actions={ACTIONS}
      onSubmit={handleSubmit}
      data={sortBy(agents, ['updatedAt'])}
    />
  )
}

export default AdminAgentItem
