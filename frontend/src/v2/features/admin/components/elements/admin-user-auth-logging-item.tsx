import Text from '@common/components/ui/text'
import React from 'react'
import { Stack } from 'rsuite'
import { AdminAction, AdminActionType } from '../../types/admin-action'
import AdminBasicItemGeneric from './admin-basic-item-generic'
import useUserAuthLoggingListQuery from '../../services/use-admin-user-auth-logging-service.tsx'

const CELLS = [
  { key: 'userId', label: 'userId', width: 60 },
  { key: 'email', label: 'nom', width: 250 },
  { key: 'eventType', label: 'eventType', width: 150 },
  { key: 'ipAddress', label: 'ipAddress', width: 150 },
  { key: 'userAgent', label: 'userAgent', width: 150 },
  { key: 'success', label: 'success', width: 150 },
  { key: 'failureReason', label: 'failureReason', width: 150 },
  { key: 'timestamp', label: 'timestamp', width: 150 }
]

const ACTIONS: AdminAction[] = []

type AdminUserAuthLoggingProps = {}

const AdminUserAuthLoggingItem: React.FC<AdminUserAuthLoggingProps> = () => {
  const { data: logs } = useUserAuthLoggingListQuery()

  return (
    <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%', height: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem" alignItems="flex-end" justifyContent="flex-start">
          <Stack.Item>
            <Text as="h1" size={30}>{`Logging authentification`}</Text>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <AdminBasicItemGeneric
          cells={CELLS}
          data={logs?.items}
          actions={ACTIONS}
          onSubmit={(action: AdminActionType, value: any) => {}}
          title={'Tout'}
        />
      </Stack.Item>
    </Stack>
  )
}

export default AdminUserAuthLoggingItem
