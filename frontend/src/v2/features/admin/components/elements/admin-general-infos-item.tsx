import Text from '@common/components/ui/text'
import { Icon, TextInput, THEME } from '@mtes-mct/monitor-ui'
import React, { useEffect, useState } from 'react'
import { Pagination, Stack } from 'rsuite'
import useAdminDeleteGeneralInfosMutation from '../../services/use-admin-delete-general-infos.tsx'
import useGeneralInfosListQuery from '../../services/use-admin-general-infos.tsx'
import useAdminUpdateGeneralInfosMutation from '../../services/use-admin-update-general-infos.tsx'
import { AdminAction, AdminActionType } from '../../types/admin-action'
import { AdminGeneralInfos } from '../../types/admin-general-infos-types.ts'
import AdminGeneralInfosForm from '../ui/admin-general-infos-form.tsx'
import AdminBasicItemGeneric from './admin-basic-item-generic'

const CELLS = [
  { key: 'id', label: 'Id', width: 60 },
  { key: 'missionId', label: 'missionId', width: 60 },
  { key: 'missionIdUUID', label: 'missionIdUUID', width: 340 },
  { key: 'serviceId', label: 'Service', width: 50 },
  { key: 'missionReportType', label: 'Type rapport', width: 140 },
  { key: 'reinforcementType', label: 'Renfort', width: 100 },
  { key: 'jdpType', label: 'JDP', width: 80 },
  { key: 'distanceInNauticalMiles', label: 'Distance (nm)', width: 100 },
  { key: 'consumedGOInLiters', label: 'GO (L)', width: 80 },
  { key: 'consumedFuelInLiters', label: 'Fuel (L)', width: 80 },
  { key: 'nbHourAtSea', label: 'Heures mer', width: 90 },
  { key: 'nbrOfRecognizedVessel', label: 'Navires', width: 70 },
  { key: 'isMissionArmed', label: 'Armée', width: 70 },
  { key: 'isWithInterMinisterialService', label: 'Intermin.', width: 80 },
  { key: 'createdAt', label: 'Création', width: 150 },
  { key: 'updatedAt', label: 'Mise à jour', width: 150 }
]

const ACTIONS: AdminAction[] = [
  {
    label: `Mise à jour`,
    key: AdminActionType.UPDATE,
    form: AdminGeneralInfosForm,
    icon: Icon.EditUnbordered
  },
  {
    label: `Supprimer général infos`,
    color: THEME.color.maximumRed,
    key: AdminActionType.DELETE,
    form: () => <>Voulez-vous vraiment supprimer cette infos général?</>,
    icon: Icon.Delete
  }
]

type AdminGeneralInfosProps = {}

const AdminGeneralInfosItem: React.FC<AdminGeneralInfosProps> = () => {
  const [page, setPage] = useState(0)
  const [pageSize, setPageSize] = useState(10)
  const [search, setSearch] = useState<string>('')
  const [debouncedSearch, setDebouncedSearch] = useState<string>('')

  // Debounce search input
  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedSearch(search)
      setPage(0) // Reset to first page when search changes
    }, 500)

    return () => clearTimeout(timer)
  }, [search])

  const { data } = useGeneralInfosListQuery(page, pageSize, debouncedSearch || undefined)
  const mutation = useAdminUpdateGeneralInfosMutation()
  const deleteMutation = useAdminDeleteGeneralInfosMutation()

  const handleSubmit = async (action: AdminActionType, value: AdminGeneralInfos) => {
    if (action !== AdminActionType.DELETE) return await mutation.mutateAsync(value)
    if (action === AdminActionType.DELETE) return await deleteMutation.mutateAsync(value.id)
  }

  const handlePageChange = (newPage: number) => {
    setPage(newPage - 1)
  }

  const handlePageSizeChange = (newPageSize: number) => {
    setPageSize(newPageSize)
    setPage(0)
  }

  const handleSearchChange = (value: string | undefined) => {
    setSearch(value || '')
  }

  return (
    <Stack direction="column" spacing="1rem" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem" alignItems="flex-end" justifyContent="flex-start">
          <Stack.Item>
            <Text as="h1" size={30}>
              GeneralInfos
            </Text>
          </Stack.Item>
          <Stack.Item style={{ width: '30%' }}>
            <TextInput
              name="search"
              value={search}
              label="Rechercher par missionId ou missionIdUUID"
              placeholder="ex: 123 ou 550e8400-e29b-41d4-a716-446655440000"
              onChange={handleSearchChange}
            />
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <AdminBasicItemGeneric
          cells={CELLS}
          title={debouncedSearch ? 'Résultats' : 'Tout'}
          actions={ACTIONS}
          onSubmit={handleSubmit}
          data={data?.items}
        />
      </Stack.Item>
      {data && data.totalPages > 0 && (
        <Stack.Item style={{ width: '100%' }}>
          <Stack direction="row" justifyContent="space-between" alignItems="center">
            <Stack.Item>
              <Text as="span">{`Total: ${data.totalItems} éléments`}</Text>
            </Stack.Item>
            <Stack.Item>
              <Pagination
                prev
                next
                first
                last
                ellipsis
                boundaryLinks
                maxButtons={5}
                size="md"
                layout={['total', '-', 'limit', '|', 'pager', 'skip']}
                total={data.totalItems}
                limitOptions={[10, 20, 50, 100]}
                limit={pageSize}
                activePage={page + 1}
                onChangePage={handlePageChange}
                onChangeLimit={handlePageSizeChange}
              />
            </Stack.Item>
          </Stack>
        </Stack.Item>
      )}
    </Stack>
  )
}

export default AdminGeneralInfosItem
