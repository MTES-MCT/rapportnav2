import Text from '@common/components/ui/text'
import { Icon, TextInput, THEME } from '@mtes-mct/monitor-ui'
import { orderBy } from 'lodash'
import React, { useMemo, useState } from 'react'
import { Stack } from 'rsuite'
import useAdminCreateOrUpdateServiceMutation from '../../services/use-admin-create-update-services-service'
import useAdminDeleteServiceMutation from '../../services/use-admin-delete-services-service'
import useAdminServiceListQuery from '../../services/use-admin-services-service'
import { AdminAction, AdminActionType } from '../../types/admin-action'
import { AdminService } from '../../types/admin-services-type'
import AdminServiceForm from '../ui/admin-service-form'
import AdminBasicItemGeneric from './admin-basic-item-generic'

const CELLS = [
  { key: 'id', label: 'Id', width: 60 },
  { key: 'name', label: 'Nom', width: 300 },
  { key: 'controlUnits', label: 'Unité de contrôle', width: 200 },
  { key: 'serviceType', label: 'Type', width: 200 },
  { key: 'createdAt', label: 'Date de création', width: 200 },
  { key: 'updatedAt', label: 'Dernière mise à jour', width: 240 },
  { key: 'deletedAt', label: 'Date de suppression', width: 200 }
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
    label: `Supprimer un service`,
    color: THEME.color.maximumRed,
    key: AdminActionType.DELETE,
    form: () => <>Voulez-vous vraiment supprimer ce service?</>,
    icon: Icon.Delete
  }
]

type AdminServiceProps = {}

const AdminServiceItem: React.FC<AdminServiceProps> = () => {
  const [search, setSearch] = useState<string>()
  const { data: services } = useAdminServiceListQuery()
  const deleteService = useAdminDeleteServiceMutation()
  const createOrUpdatemutation = useAdminCreateOrUpdateServiceMutation()

  const handleSubmit = async (action: AdminActionType, value: AdminService) => {
    if (action === AdminActionType.DELETE) await deleteService.mutateAsync(value.id)
    if (action !== AdminActionType.DELETE) await createOrUpdatemutation.mutateAsync(value)
  }

  const filterServices = (searchValue: string, values?: AdminService[]) =>
    values?.filter(service => {
      const query = searchValue.toLowerCase().trim()
      const name = service.name?.toLowerCase() ?? ''
      return name.includes(query)
    })

  const filteredServices = useMemo(() => {
    return orderBy(
      search?.trim() ? filterServices(search, services) : services,
      [obj => new Date(obj.updatedAt ?? 0)],
      ['desc']
    )
  }, [services, search])

  return (
    <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%', height: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem" alignItems="flex-end" justifyContent="flex-start">
          <Stack.Item>
            <Text as="h1" size={30}>{`Services`}</Text>
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
          onSubmit={handleSubmit}
          data={filteredServices}
          title={search?.trim() ? 'Filtrés' : 'Tout'}
        />
      </Stack.Item>
    </Stack>
  )
}

export default AdminServiceItem
