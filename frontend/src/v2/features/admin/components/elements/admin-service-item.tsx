import React from 'react'
import useAdminCreateOrUpdateServiceMutation from '../../services/use-admin-create-or-update-services-service'
import useAdminServiceListQuery from '../../services/use-admin-services-service'
import { AdminAction, AdminService } from '../../types/admin-services-type'
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

type AdminServiceProps = {}

const AdminServiceItem: React.FC<AdminServiceProps> = () => {
  const { data: services } = useAdminServiceListQuery()
  const createOrUpdatemutation = useAdminCreateOrUpdateServiceMutation()

  const handleSubmit = async (action: AdminAction, value: AdminService) => {
    if (action !== 'DELETE') await createOrUpdatemutation.mutateAsync(value)
  }

  return (
    <AdminBasicItemGeneric
      cells={CELLS}
      module="Services"
      data={services}
      onSubmit={handleSubmit}
      form={AdminServiceForm}
      mainButtonLabel={`Créer un service`}
    />
  )
}

export default AdminServiceItem
