import Text from '@common/components/ui/text'
import { Icon, THEME } from '@mtes-mct/monitor-ui'
import React, { useState } from 'react'
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
  { key: 'startDateTimeUtc', label: 'Début', width: 170 },
  { key: 'endDateTimeUtc', label: 'Fin', width: 170 },
  { key: 'missionSource', label: 'Source', width: 130 },
  { key: 'openBy', label: 'Ouverte par', width: 110 },
  { key: 'completedBy', label: 'Complétée par', width: 110 },
  { key: 'isDeleted', label: 'Supprimée', width: 90 },
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

  const { data } = useMissionsListQuery(page, pageSize)
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

  return (
    <Stack direction="column" spacing="1rem" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Text as="h1" size={30}>
          Missions
        </Text>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <AdminBasicItemGeneric
          cells={CELLS}
          title={'Tout'}
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
