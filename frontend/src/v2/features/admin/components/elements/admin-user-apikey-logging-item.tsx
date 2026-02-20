import Text from '@common/components/ui/text'
import React from 'react'
import { Stack } from 'rsuite'
import { AdminAction, AdminActionType } from '../../types/admin-action'
import AdminBasicItemGeneric from './admin-basic-item-generic'
import useUserAuthLoggingListQuery from '../../services/use-admin-user-auth-logging-service.tsx'
import useApiKeyLoggingListQuery from '../../services/use-admin-apikey-logging-service.tsx'

const CELLS = [
  { key: 'apiKeyId', label: 'apiKeyId', width: 200 },
  { key: 'ipAddress', label: 'ipAddress', width: 200 },
  { key: 'requestPath', label: 'requestPath', width: 200 },
  { key: 'success', label: 'success', width: 200 },
  { key: 'failureReason', label: 'failureReason', width: 200 },
  { key: 'timestamp', label: 'timestamp', width: 200 }
]

const ACTIONS: AdminAction[] = []

type AdminApiKeyLoggingProps = {}

const AdminApiKeyLoggingItem: React.FC<AdminApiKeyLoggingProps> = () => {
  const { data: logs } = useApiKeyLoggingListQuery()

  return (
    <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%', height: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem" alignItems="flex-end" justifyContent="flex-start">
          <Stack.Item>
            <Text as="h1" size={30}>{`Logging API Keys usage`}</Text>
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

export default AdminApiKeyLoggingItem
