import Text from '@common/components/ui/text'
import { Icon, TextInput, THEME } from '@mtes-mct/monitor-ui'
import React, { useEffect, useState } from 'react'
import { Pagination, Stack } from 'rsuite'
import { BasicAction, AdminActionType } from '../../../common/types/basic-action.ts'
import useAdminDeleteMissionMutation from '../../services/use-admin-delete-mission.tsx'
import useMissionsListQuery from '../../services/use-admin-missions.tsx'
import { AdminMission } from '../../types/admin-mission-types.ts'
import AdminBasicItemGeneric from './admin-basic-item-generic'

const CELLS = [
  { key: 'id', label: 'Id', width: 320 },
  { key: 'serviceId', label: 'Service', width: 70 },
  { key: 'externalId', label: 'External Id', width: 120 },
  { key: 'startDateTimeUtc', label: 'Début', width: 170, type: { format: 'datetime' } },
  { key: 'endDateTimeUtc', label: 'Fin', width: 170, type: { format: 'datetime' } },
  { key: 'missionSource', label: 'Source', width: 130 },
  { key: 'openBy', label: 'Ouverte par', width: 110 },
  { key: 'completedBy', label: 'Complétée par', width: 110 },
  { key: 'isDeleted', label: 'Supprimée', width: 90 },
  { key: 'isCompleteForStats', label: 'Complétude stats', width: 130 },
  { key: 'sourcesOfMissingData', label: 'Sources données manquantes', width: 200 },
  { key: 'createdAt', label: 'Création', width: 170 },
  { key: 'updatedAt', label: 'Mise à jour', width: 170 }
]

const ACTIONS: BasicAction[] = [
  {
    label: `Supprimer la mission`,
    color: THEME.color.maximumRed,
    key: AdminActionType.DELETE,
    form: () => <>Voulez-vous vraiment supprimer cette mission?</>,
    icon: Icon.Delete
  }
]

type AdminMissionProps = {}

const AdminMissionItem: React.FC<AdminMissionProps> = () => {
  const [page, setPage] = useState(0)
  const [pageSize, setPageSize] = useState(10)
  const [searchId, setSearchId] = useState<string>('')
  const [debouncedSearch, setDebouncedSearch] = useState<string>('')

  // Debounce search input
  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedSearch(searchId)
      setPage(0) // Reset to first page when search changes
    }, 500)

    return () => clearTimeout(timer)
  }, [searchId])

  const { data } = useMissionsListQuery(page, pageSize, debouncedSearch || undefined)
  const deleteMutation = useAdminDeleteMissionMutation()

  const handleSubmit = async (action: AdminActionType, value: AdminMission) => {
    if (action === AdminActionType.DELETE) return await deleteMutation.mutateAsync(value.id)
  }

  const handlePageChange = (newPage: number) => {
    setPage(newPage - 1)
  }

  const handlePageSizeChange = (newPageSize: number) => {
    setPageSize(newPageSize)
    setPage(0)
  }

  const handleSearchIdChange = (value: string | undefined) => {
    setSearchId(value || '')
  }

  return (
    <Stack direction="column" spacing="1rem" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem" alignItems="flex-end" justifyContent="flex-start">
          <Stack.Item>
            <Text as="h1" size={30}>
              Missions
            </Text>
          </Stack.Item>
          <Stack.Item style={{ width: '40%' }}>
            <TextInput
              name="searchId"
              value={searchId}
              label="Rechercher par id (UUID) ou external id"
              placeholder="ex: 550e8400-e29b-41d4-a716-446655440000 ou 12345"
              onChange={handleSearchIdChange}
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
              <Text as="h4">{`Total: ${data.totalItems} éléments`}</Text>
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

export default AdminMissionItem
