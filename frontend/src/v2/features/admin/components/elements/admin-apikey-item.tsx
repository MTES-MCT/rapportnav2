import Text from '@common/components/ui/text.tsx'
import { Icon, TextInput, THEME } from '@mtes-mct/monitor-ui'
import { orderBy } from 'lodash'
import React, { useMemo, useState } from 'react'
import { Stack } from 'rsuite'
import useApiKeyListQuery from '../../services/use-admin-apikeys-service.tsx'
import useAdminCreateOrUpdateApiKeyMutation from '../../services/use-admin-create-update-apikey-service.tsx'
import useAdminDisableApiKeyMutation from '../../services/use-admin-disable-apikey-service.tsx'
import useAdminRotateApiKeyMutation from '../../services/use-admin-rorate-apikey-service.tsx'
import { AdminAction, AdminActionType } from '../../types/admin-action'
import { ApiKey } from '../../types/admin-apikey-types.ts'
import AdminApikeyForm from '../ui/admin-apikey-form.tsx'
import AdminBasicItemGeneric from './admin-basic-item-generic'

const CELLS = [
  { key: 'id', label: 'Id', width: 400 },
  { key: 'publicId', label: 'PublicId', width: 200 },
  { key: 'owner', label: 'Owner', width: 300 },
  { key: 'createdAt', label: 'Date de Creation', width: 200 },
  { key: 'updatedAt', label: 'Dernière mise à jour', width: 200 },
  { key: 'lastUsedAt', label: 'Dernière utilisation', width: 200 },
  { key: 'disabledAt', label: 'Date de désactivation', width: 200 }
]

const ACTIONS: AdminAction[] = [
  {
    isMain: true,
    label: `Créer une api-key`,
    key: AdminActionType.CREATE,
    form: AdminApikeyForm
  },
  {
    label: `Rotationner une api-key`,
    color: THEME.color.maximumRed,
    key: AdminActionType.ROTATE_KEY,
    form: () => <>{`Voulez-vous vraiment rotationner cet API Key?`}</>,
    icon: Icon.Reset
  },
  {
    label: `Désactiver une api-key`,
    color: THEME.color.maximumRed,
    key: AdminActionType.DELETE,
    form: () => <>{`Voulez-vous vraiment supprimer cet API Key?`}</>,
    icon: Icon.Delete
  }
]

type AdminUserProps = {}

const AdminApiKeyItem: React.FC<AdminUserProps> = () => {
  const { data: apiKeys } = useApiKeyListQuery()
  const [search, setSearch] = useState<string>()
  const createOrUpdateApiKeyMutation = useAdminCreateOrUpdateApiKeyMutation()
  const disableApiKeyMutation = useAdminDisableApiKeyMutation()
  const rotateApiKeyMutation = useAdminRotateApiKeyMutation()

  const handleSubmit = async (action: AdminActionType, value: ApiKey) => {
    switch (action) {
      case AdminActionType.CREATE:
        await createOrUpdateApiKeyMutation.mutateAsync(value)
        break
      case AdminActionType.ROTATE_KEY:
        await rotateApiKeyMutation.mutateAsync(value)
        break
      case AdminActionType.DELETE:
        await disableApiKeyMutation.mutateAsync(value)
        break
      default:
        break
    }
  }

  const filterApiKeys = (searchValue: string, values?: ApiKey[]) =>
    values?.filter(key => {
      const query = searchValue.toLowerCase().trim()
      const owner = key.owner?.toLowerCase() ?? ''
      return owner.includes(query)
    })

  const filteredApiKeys = useMemo(() => {
    return orderBy(
      search?.trim() ? filterApiKeys(search, apiKeys) : apiKeys,
      [obj => new Date(obj.updatedAt ?? 0)],
      ['desc']
    )
  }, [apiKeys, search])

  return (
    <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%', height: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem" alignItems="flex-end" justifyContent="flex-start">
          <Stack.Item>
            <Text as="h1" size={30}>{`API Keys`}</Text>
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
          data={filteredApiKeys}
          onSubmit={handleSubmit}
          title={search?.trim() ? 'Filtrés' : 'Tout'}
        />
      </Stack.Item>
    </Stack>
  )
}

export default AdminApiKeyItem
