import Text from '@common/components/ui/text'
import { TextInput } from '@mtes-mct/monitor-ui'
import React, { useEffect, useState } from 'react'
import { Pagination, Stack } from 'rsuite'
import { AdminActionType, BasicAction } from '../../../common/types/basic-action.ts'
import useMissionActionsListQuery from '../../services/use-admin-mission-actions.tsx'
import AdminBasicItemGeneric from './admin-basic-item-generic'

const CELLS = [
  { key: 'id', label: 'Id', width: 320 },
  { key: 'ownerId', label: 'Owner Id', width: 320 },
  { key: 'startDateTimeUtc', label: 'Début', width: 170, type: { format: 'datetime' } },
  { key: 'endDateTimeUtc', label: 'Fin', width: 170, type: { format: 'datetime' } },
  { key: 'actionType', label: 'Type', width: 160 },
  { key: 'status', label: 'Statut', width: 120 },
  { key: 'isCompleteForStats', label: 'Complétude stats', width: 130 }
]

// Read-only listing: no row actions.
const ACTIONS: BasicAction[] = []

type AdminMissionActionProps = {}

const AdminMissionActionItem: React.FC<AdminMissionActionProps> = () => {
  const [page, setPage] = useState(0)
  const [pageSize, setPageSize] = useState(10)
  const [searchId, setSearchId] = useState<string>('')
  const [searchOwnerId, setSearchOwnerId] = useState<string>('')
  const [debouncedSearchId, setDebouncedSearchId] = useState<string>('')
  const [debouncedSearchOwnerId, setDebouncedSearchOwnerId] = useState<string>('')

  // Debounce search inputs
  useEffect(() => {
    const timer = setTimeout(() => {
      setDebouncedSearchId(searchId)
      setDebouncedSearchOwnerId(searchOwnerId)
      setPage(0) // Reset to first page when search changes
    }, 500)

    return () => clearTimeout(timer)
  }, [searchId, searchOwnerId])

  const { data } = useMissionActionsListQuery(
    page,
    pageSize,
    debouncedSearchId || undefined,
    debouncedSearchOwnerId || undefined
  )

  const handleSubmit = async () => {}

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

  const handleSearchOwnerIdChange = (value: string | undefined) => {
    setSearchOwnerId(value || '')
  }

  return (
    <Stack direction="column" spacing="1rem" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem" alignItems="flex-end" justifyContent="flex-start">
          <Stack.Item>
            <Text as="h1" size={30}>
              Mission Actions
            </Text>
          </Stack.Item>
          <Stack.Item style={{ width: '30%' }}>
            <TextInput
              name="searchId"
              value={searchId}
              label="Rechercher par id (UUID)"
              placeholder="ex: 550e8400-e29b-41d4-a716-446655440000"
              onChange={handleSearchIdChange}
            />
          </Stack.Item>
          <Stack.Item style={{ width: '30%' }}>
            <TextInput
              name="searchOwnerId"
              value={searchOwnerId}
              label="Rechercher par ownerId (UUID)"
              placeholder="ex: 550e8400-e29b-41d4-a716-446655440000"
              onChange={handleSearchOwnerIdChange}
            />
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <AdminBasicItemGeneric
          cells={CELLS}
          title={debouncedSearchId || debouncedSearchOwnerId ? 'Résultats' : 'Tout'}
          actions={ACTIONS}
          onSubmit={handleSubmit as (action: AdminActionType, value: any) => Promise<void>}
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

export default AdminMissionActionItem
