import Text from '@common/components/ui/text.tsx'
import { Icon, TextInput, THEME } from '@mtes-mct/monitor-ui'
import React, { useState } from 'react'
import { Stack } from 'rsuite'
import { AdminAction, AdminActionType } from '../../types/admin-action'
import AdminBasicItemGeneric from './admin-basic-item-generic'
import AdminFishAuctionForm from '../ui/admin-fish-auction-form.tsx'
import useAdminFishAuctionListQuery from '../../services/use-admin-fish-auctions-service.tsx'
import useAdminCreateOrUpdateFishAuctionMutation from '../../services/use-admin-create-update-fish-auction-service.tsx'
import useAdminDisableFishAuctionMutation from '../../services/use-admin-disable-fish-auction.tsx'

const CELLS = [
  { key: 'id', label: 'Id', width: 32 },
  { key: 'name', label: 'Nom', width: 400 },
  { key: 'facade', label: 'Façade', width: 100 },
  { key: 'createdAt', label: 'Date de Creation', width: 200 },
  { key: 'updatedAt', label: 'Dernière mise à jour', width: 200 },
  { key: 'deletedAt', label: 'Date de désactivation', width: 200 }
]

const ACTIONS: AdminAction[] = [
  {
    isMain: true,
    label: `Créer une crée`,
    key: AdminActionType.CREATE,
    form: AdminFishAuctionForm
  },
  {
    label: `Mise à jour d'une criée`,
    key: AdminActionType.UPDATE,
    form: AdminFishAuctionForm,
    icon: Icon.EditUnbordered
  },
  {
    label: `Désactiver une criée`,
    color: THEME.color.maximumRed,
    key: AdminActionType.DELETE,
    form: () => <>{`Voulez-vous vraiment supprimer cette criée?`}</>,
    icon: Icon.Delete
  }
]

type AdminUserProps = {}

const AdminApiKeyItem: React.FC<AdminUserProps> = () => {
  const { data } = useAdminFishAuctionListQuery()
  const [search, setSearch] = useState<string>()
  const createOrUpdateFishAuctionMutation = useAdminCreateOrUpdateFishAuctionMutation()
  const disableFishAuctionMutation = useAdminDisableFishAuctionMutation()

  const handleSubmit = async (action: AdminActionType, value: AdminActionType) => {
    switch (action) {
      case AdminActionType.CREATE:
        await createOrUpdateFishAuctionMutation.mutateAsync(value)
        break
      case AdminActionType.UPDATE:
        await createOrUpdateFishAuctionMutation.mutateAsync(value)
        break
      case AdminActionType.DELETE:
        await disableFishAuctionMutation.mutateAsync(value.id)
        break
      default:
        break
    }
  }

  return (
    <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%', height: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem" alignItems="flex-end" justifyContent="flex-start">
          <Stack.Item>
            <Text as="h1" size={30}>{`Liste des Criées`}</Text>
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
          data={data}
          onSubmit={handleSubmit}
          title={search?.trim() ? 'Filtrés' : 'Tout'}
        />
      </Stack.Item>
    </Stack>
  )
}

export default AdminApiKeyItem
