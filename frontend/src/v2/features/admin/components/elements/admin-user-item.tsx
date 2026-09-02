import Text from '@common/components/ui/text'
import { Accent, Icon, TextInput, THEME } from '@mtes-mct/monitor-ui'
import { orderBy } from 'lodash'
import React, { useMemo, useState } from 'react'
import { Stack } from 'rsuite'
import { AdminActionType, BasicAction } from '../../../common/types/basic-action'
import { User } from '../../../common/types/user'
import useAdminCreateOrUpdateUserMutation from '../../services/use-admin-create-update-user-service'
import useAdminDisableUserMutation from '../../services/use-admin-disable-user'
import useAdminUserPasswordMutation from '../../services/use-admin-update-user-password-service'
import useAdminServiceListQuery from '../../services/use-admin-services-service'
import useUserListQuery from '../../services/use-admin-user-service'
import { AdminAgent, AdminUser } from '../../types/admin-agent-types'
import AdminUserDeleteForm from '../ui/admin-user-delete-form'
import AdminUserForm from '../ui/admin-user-form'
import AdminUserPasswordForm from '../ui/admin-user-password-form'
import AdminBasicItemGeneric from './admin-basic-item-generic'

const CELLS = [
  { key: 'id', label: 'Id', width: 60 },
  { key: 'email', label: 'nom', width: 250 },
  { key: 'firstName', label: 'prénom', width: 120 },
  { key: 'lastName', label: 'nom', width: 120 },
  { key: 'roles', label: 'Rôles', width: 200 },
  { key: 'service', label: 'Service', width: 150 },
  { key: 'createdAt', label: 'Date de Creation', width: 150 },
  { key: 'updatedAt', label: 'Dernière mise à jour', width: 150 },
  { key: 'disabledAt', label: 'Dernière de désactivation', width: 120 }
]

const ACTIONS: BasicAction[] = [
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
    label: `Reactivate user`,
    icon: Icon.Load,
    key: AdminActionType.REACTIVATE_USER,
    disabled: (rowData: unknown) => !(rowData as AdminAgent).disabledAt,
    form: ({ formik }) => <>{`Voulze-vous reactiver ${formik?.values?.firstName}, ${formik?.values?.lastName}?`}</>
  },
  {
    icon: Icon.Delete,
    accent: Accent.ERROR,
    title: 'Supprimer',
    form: AdminUserDeleteForm,
    color: THEME.color.maximumRed,
    key: AdminActionType.DISABLE_USER,
    validateButton: `Désactiver l'utilisateur`,
    label: `Désactiver un utilisateur`
  }
]

type AdminUserProps = {}

const AdminUserItem: React.FC<AdminUserProps> = () => {
  const { data: users } = useUserListQuery()
  const { data: services } = useAdminServiceListQuery()
  const [search, setSearch] = useState<string>()

  const serviceNameById = useMemo(() => new Map(services?.map(s => [s.id, s.name])), [services])

  const updatePasswordMutation = useAdminUserPasswordMutation()
  const createOrUpdateMutation = useAdminCreateOrUpdateUserMutation()
  const disableUserMutation = useAdminDisableUserMutation()

  const handleSubmit = async (action: AdminActionType, value: AdminUser) => {
    if (action === AdminActionType.UPDATE_PASSWORD && value.password) {
      await updatePasswordMutation.mutateAsync({ userId: value.id, password: value.password })
      return
    }
    if (action === AdminActionType.DISABLE_USER) {
      await disableUserMutation.mutateAsync(value.id)
      return
    }
    if (action === AdminActionType.REACTIVATE_USER) {
      value.disabledAt = undefined
    }
    await createOrUpdateMutation.mutateAsync(value)
  }

  const filterUsers = (searchValue: string, values?: User[]) =>
    values?.filter(user => {
      const query = searchValue.toLowerCase().trim()
      const firstName = user.firstName?.toLowerCase() ?? ''
      const lastName = user.lastName?.toLowerCase() ?? ''
      const fullName = `${firstName} ${lastName}`.toLowerCase()
      return firstName.includes(query) || lastName.includes(query) || fullName.includes(query)
    })

  const filteredUsers = useMemo(() => {
    const filtered = search?.trim() ? filterUsers(search, users) : users
    const enriched = filtered?.map(user => ({
      ...user,
      service: user.serviceId != null ? (serviceNameById.get(user.serviceId) ?? '') : ''
    }))
    return orderBy(enriched, [obj => new Date(obj.updatedAt ?? 0)], ['desc'])
  }, [users, search, serviceNameById])

  return (
    <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%', height: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem" alignItems="flex-end" justifyContent="flex-start">
          <Stack.Item>
            <Text as="h1" size={30}>{`Utilisateurs`}</Text>
          </Stack.Item>
          <Stack.Item style={{ width: '30%' }}>
            <TextInput name="search" value={search} label="Rechercher" onChange={nextValue => setSearch(nextValue)} />
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <AdminBasicItemGeneric
          cells={CELLS}
          data={filteredUsers}
          actions={ACTIONS}
          onSubmit={handleSubmit}
          title={search?.trim() ? 'Filtrés' : 'Tout'}
        />
      </Stack.Item>
    </Stack>
  )
}

export default AdminUserItem
