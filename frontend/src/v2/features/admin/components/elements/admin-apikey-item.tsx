import { Icon, THEME } from '@mtes-mct/monitor-ui'
import React from 'react'
import { AdminAction, AdminActionType } from '../../types/admin-action'
import AdminBasicItemGeneric from './admin-basic-item-generic'
import useAdminCreateOrUpdateApiKeyMutation from '../../services/use-admin-create-update-apikey-service.tsx'
import useApiKeyListQuery from '../../services/use-admin-apikeys-service.tsx'
import AdminApikeyForm from '../ui/admin-apikey-form.tsx'
import useAdminDisableApiKeyMutation from '../../services/use-admin-disable-apikey-service.tsx'
import { ApiKey } from '../../types/admin-apikey-types.ts'
import useAdminRotateApiKeyMutation from '../../services/use-admin-rorate-apikey-service.tsx'

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

  return (
    <AdminBasicItemGeneric cells={CELLS} title="API Keys" data={apiKeys} actions={ACTIONS} onSubmit={handleSubmit} />
  )
}

export default AdminApiKeyItem
