import { Accent, Icon, Link, Message, THEME } from '@mtes-mct/monitor-ui'
import { useSelector } from '@tanstack/react-store'
import { Stack } from 'rsuite'
import { store } from '../../../../store'
import { AdminAgentInput } from '../../../admin/types/admin-agent-types'
import { AdminActionType, BasicAction } from '../../../common/types/basic-action'
import { User } from '../../../common/types/user'
import useGetManageAgentService from '../../services/use-manage-agent-service'
import useManageDeleteAgentMutation from '../../services/use-manage-delete-agent-service'
import useManageUpsertAgentMutation from '../../services/use-manage-upsert-agent-service'
import ManageAgentDeleteForm from '../ui/manage-agent-delete-form'
import ManageAgentForm from '../ui/manage-agent-form'
import ManageBasicItemGeneric from './manage-basic-item-generic'

const mailTo = import.meta.env.MAILTO_RAPPORTNAV_SUPPORT

const CELLS = [
  { key: 'name', label: 'Prénom, nom', width: 250 },
  { key: 'role', label: 'Rôle', width: 200 },
  { key: 'cardId', label: 'N° carte de service', width: 200 }
]

const MAIN_ACTION: BasicAction = {
  icon: Icon.Plus,
  form: ManageAgentForm,
  label: `Ajouter un nouveau membre`,
  key: AdminActionType.MANAGE_CREATE,
  validateButton: `Ajouter un member à mon unité`
}

const ACTIONS: BasicAction[] = [
  {
    title: 'Editer',
    icon: Icon.Edit,
    form: ManageAgentForm,
    label: `Modifier un membre`,
    key: AdminActionType.MANAGE_UPDATE,
    validateButton: `Valider la modification`
  },
  {
    icon: Icon.Delete,
    accent: Accent.ERROR,
    title: 'Supprimer',
    form: ManageAgentDeleteForm,
    color: THEME.color.maximumRed,
    key: AdminActionType.MANAGE_DELETE,
    validateButton: `Supprimer le membre`,
    label: `Supprimer un membre de l'unité`
  }
]

type ManageCrewItemProps = {
  user: User
}

const ManageCrewItem: React.FC<ManageCrewItemProps> = () => {
  const user = useSelector(store, state => state.user)
  const upsertAgent = useManageUpsertAgentMutation(user?.serviceId)
  const deleteAgent = useManageDeleteAgentMutation(user?.serviceId)
  const { data: agents } = useGetManageAgentService(user?.serviceId)

  const handleSubmit = async (action: AdminActionType, value: AdminAgentInput) => {
    if (action !== AdminActionType.MANAGE_DELETE) await upsertAgent.mutateAsync(value)
    if (action === AdminActionType.MANAGE_DELETE) await deleteAgent.mutateAsync(value.id)
  }

  return (
    <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%', height: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Stack direction="row" spacing="1rem" alignItems="flex-end" justifyContent="flex-start">
          <Stack.Item>
            <Message level="INFO">
              Gestion des accès à Rapport Nav <br />
              <p>
                Sur cette page vous pouvez gérer les arrivées et départs des membres de votre unité, compléter des
                informations ou corriger des erreurs de saisie (nom, prénom, n° de carte de service). En revanche, pour
                toute nouvelle demande d’accès à RapportNav pour un agent (identifiant - mot de passe),
                {mailTo && (
                  <Link
                    style={{ marginLeft: '0.5rem' }}
                    href={`mailto:${mailTo}?subject=[RAPPORT NAV] - demande d’accès - ${user?.serviceName}`}
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
        <ManageBasicItemGeneric
          cells={CELLS}
          actions={ACTIONS}
          mainAction={MAIN_ACTION}
          onSubmit={handleSubmit}
          data={agents?.map(agent => ({
            ...agent,
            agentId: agent.id,
            roleId: agent.role?.id,
            serviceId: agent.service?.id,
            role: `${agent.role?.title}`,
            name: `${agent?.firstName} ${agent?.lastName}`
          }))}
        />
      </Stack.Item>
    </Stack>
  )
}

export default ManageCrewItem
