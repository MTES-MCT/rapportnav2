import { Icon, THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import useAdminCreateOrUpdateUserMutation from '../../services/use-admin-create-update-user-service'
import useAdminUserPasswordMutation from '../../services/use-admin-update-user-password-service'
import useUserListQuery from '../../services/use-admin-user-service'
import { AdminAction, AdminActionType } from '../../types/admin-action'
import { AdminUser } from '../../types/admin-agent-types'
import AdminUserForm from '../ui/admin-user-form'
import AdminUserPasswordForm from '../ui/admin-user-password-form'
import AdminBasicItemGeneric from './admin-basic-item-generic'

const CELLS = [
  { key: 'id', label: 'Id', width: 60 },
  { key: 'email', label: 'nom', width: 250 },
  { key: 'firstName', label: 'prénom', width: 150 },
  { key: 'lastName', label: 'nom', width: 150 },
  { key: 'roles', label: 'Rôles', width: 350 },
  { key: 'serviceId', label: 'Service', width: 60 },
  { key: 'createdAt', label: 'Date de Creation', width: 200 },
  { key: 'updatedAt', label: 'Dernière mise à jour', width: 240 }
]

const ACTIONS: AdminAction[] = [
  {
    isMain: true,
    label: `Créer un user`,
    key: AdminActionType.CREATE_USER,
    form: AdminUserForm
  },
  {
    label: `Mise à jour d'un agent`,
    key: AdminActionType.UPDATE,
    form: AdminUserForm,
    icon: Icon.EditUnbordered
  },
  {
    label: `Mettre à jour le mot de passe`,
    key: AdminActionType.UPDATE_PASSWORD,
    form: AdminUserPasswordForm,
    icon: Icon.Hide
  },
  {
    disabled: () => true,
    label: `Supprimer un utilisateur`,
    color: THEME.color.maximumRed,
    key: AdminActionType.DELETE,
    form: () => <>{`Voulez-vous vraiment supprimer cet utilisateur?`}</>,
    icon: Icon.Delete
  }
]

type AdminUserProps = {}

const AdminUserItem: React.FC<AdminUserProps> = () => {
  const { data: users } = useUserListQuery()
  const updatePasswordMutation = useAdminUserPasswordMutation()
  const createOrUpdateMutation = useAdminCreateOrUpdateUserMutation()
  const handleSubmit = async (action: AdminActionType, value: AdminUser) => {
    if (action === AdminActionType.UPDATE_PASSWORD && value.password) {
      updatePasswordMutation.mutateAsync({ userId: value.id, password: value.password })
      return
    }
    if (action !== AdminActionType.DELETE) await createOrUpdateMutation.mutateAsync(value)
  }

  return <AdminBasicItemGeneric cells={CELLS} title="Users" data={users} actions={ACTIONS} onSubmit={handleSubmit} />
}

export default AdminUserItem
