import { Icon } from '@mtes-mct/monitor-ui'
import { sortBy } from 'lodash'
import React from 'react'
import { AdminAction, AdminActionType } from '../../types/admin-action'
import AdminBasicItemGeneric from './admin-basic-item-generic'
import useGeneralInfosListQuery from '../../services/use-admin-general-infos.tsx'
import useAdminUpdateGeneralInfosMutation from '../../services/use-admin-update-general-infos.tsx'
import { AdminGeneralInfos } from '../../types/admin-general-infos-types.ts'
import AdminGeneralInfosForm from '../ui/admin-general-infos-form.tsx'

const CELLS = [
  { key: 'id', label: 'Id', width: 60 },
  { key: 'missionId', label: 'missionId', width: 300 },
  { key: 'missionIdUUID', label: 'missionIdUUID', width: 300 },
  { key: 'service.id', label: 'serviceId', width: 200 },
  { key: 'createdAt', label: 'Date de création', width: 200 },
  { key: 'updatedAt', label: 'Dernière mise à jour', width: 240 },
  { key: 'deletedAt', label: 'Date de suppression', width: 200 }
]

const ACTIONS: AdminAction[] = [
  {
    label: `Mise à jour`,
    key: AdminActionType.UPDATE,
    form: AdminGeneralInfosForm,
    icon: Icon.EditUnbordered
  }
]

type AdminServiceProps = {}

const AdminServiceItem: React.FC<AdminServiceProps> = () => {
  const { data } = useGeneralInfosListQuery()
  debugger
  const mutation = useAdminUpdateGeneralInfosMutation()

  const handleSubmit = async (action: AdminActionType, value: AdminGeneralInfos) => {
    if (action !== AdminActionType.DELETE) await mutation.mutateAsync(value)
  }

  return (
    <AdminBasicItemGeneric
      cells={CELLS}
      title="GeneralInfos"
      actions={ACTIONS}
      onSubmit={handleSubmit}
      data={sortBy(data, ['createdAt'])}
    />
  )
}

export default AdminServiceItem
