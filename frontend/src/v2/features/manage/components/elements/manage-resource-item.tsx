import { Icon, Label, Link, Message } from '@mtes-mct/monitor-ui'
import { useSelector } from '@tanstack/react-store'
import { Stack } from 'rsuite'
import { store } from '../../../../store'
import useResourceByControlUnitQuery from '../../../common/services/use-resources-control-unit'
import { AdminActionType, BasicAction } from '../../../common/types/basic-action'
import { ResourceInput } from '../../../common/types/control-unit-types'
import { User } from '../../../common/types/user'
import useManageUpsertResourceMutation from '../../services/use-manage-upsert-resource-service'
import ManageResourceForm from '../ui/manage-resource-form'
import ManageBasicItemGeneric from './manage-basic-item-generic'

const mailTo = import.meta.env.MAILTO_SUPPORT_RESOURCE

const CELLS = [
  { key: 'name', label: 'Nom du moyen', width: 250 },
  { key: 'registrationId', label: 'Marque externe/immat', width: 200 },
  { key: 'radioFrequency', label: 'indicatif appel radio', width: 200 }
]

const ACTIONS: BasicAction[] = [
  {
    title: 'Editer',
    icon: Icon.Edit,
    form: ManageResourceForm,
    label: `Modifier un moyen`,
    key: AdminActionType.MANAGE_UPDATE,
    validateButton: `Valider la modification`
  }
]

type ManageResourceItemProps = {
  user: User
}

const ManageResourceItem: React.FC<ManageResourceItemProps> = () => {
  const user = useSelector(store, state => state.user)
  const { resources } = useResourceByControlUnitQuery(user?.controlUnitId)
  const upsertResource = useManageUpsertResourceMutation(user?.serviceId)

  const handleSubmit = async (action: AdminActionType, value: ResourceInput) => {
    await upsertResource.mutateAsync(value)
  }

  return (
    <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%', height: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem" alignItems="flex-end" justifyContent="flex-start">
          <Stack.Item>
            <Message level="INFO">
              <Label>Gestion des accès à Rapport Nav</Label> <br />
              <p>
                Sur cette page vous pouvez compléter des informations liées à vos moyens (immatriculation, indicatif
                appel radio) afin de les remplir automatiquement dans vos formulaires de contrôle.
                <br />
                En revanche, pour toute demande d’ajout/suppression de moyen pour l’unité,
                {mailTo && (
                  <Link
                    style={{ marginLeft: '0.5rem' }}
                    href={`mailto:${mailTo}?subject=[${user?.serviceName}] - demande d’ajout/suppression de moyen`}
                  >
                    cliquer ici
                  </Link>
                )}
              </p>
            </Message>
          </Stack.Item>
        </Stack>
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <ManageBasicItemGeneric cells={CELLS} data={resources} actions={ACTIONS} onSubmit={handleSubmit} />
      </Stack.Item>
    </Stack>
  )
}
export default ManageResourceItem
