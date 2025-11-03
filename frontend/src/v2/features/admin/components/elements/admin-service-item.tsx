import { Icon, THEME } from '@mtes-mct/monitor-ui'
import { sortBy } from 'lodash'
import React from 'react'
import useAdminCreateOrUpdateServiceMutation from '../../services/use-admin-create-update-services-service'
import useAdminServiceListQuery from '../../services/use-admin-services-service'
import { AdminAction, AdminActionType } from '../../types/admin-action'
import { AdminService } from '../../types/admin-services-type'
import AdminServiceForm from '../ui/admin-service-form'
import AdminBasicItemGeneric from './admin-basic-item-generic'

const CELLS = [
  { key: 'id', label: 'Id', width: 60 },
  { key: 'name', label: 'Nom', width: 300 },
  { key: 'controlUnits', label: 'Unité de contrôle', width: 200 },
  { key: 'createdAt', label: 'Date de création', width: 200 },
  { key: 'updatedAt', label: 'Dernière mise à jour', width: 240 },
  { key: 'deletedAt', label: 'Date de supression', width: 200 }
]

const ACTIONS: AdminAction[] = [
  {
    isMain: true,
    label: `Créer un service`,
    key: AdminActionType.CREATE,
    form: AdminServiceForm
  },
  {
    label: `Mise à jour d'un service`,
    key: AdminActionType.UPDATE,
    form: AdminServiceForm,
    icon: Icon.EditUnbordered
  },
  {
    disabled: true,
    label: `Supprimer un service`,
    color: THEME.color.maximumRed,
    key: AdminActionType.DELETE,
    form: () => <>Voulez-vous vraiment supprimer ce service?</>,
    icon: Icon.Delete
  }
]

type AdminServiceProps = {}

const AdminServiceItem: React.FC<AdminServiceProps> = () => {
  const { data: services } = useAdminServiceListQuery()
  const createOrUpdatemutation = useAdminCreateOrUpdateServiceMutation()

  const handleSubmit = async (action: AdminActionType, value: AdminService) => {
    if (action !== AdminActionType.DELETE) await createOrUpdatemutation.mutateAsync(value)
  }

  return (
    <AdminBasicItemGeneric
      cells={CELLS}
      title="Services"
      actions={ACTIONS}
      onSubmit={handleSubmit}
      data={sortBy(services, ['updatedAt'])}
    />
  )
}

export default AdminServiceItem
