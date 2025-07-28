import { Accent, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { useGlobalRoutes } from '@router/use-global-routes'
import { useNavigate } from 'react-router-dom'
import { Stack } from 'rsuite'
import useDeleteActionMutation from '../../services/use-delete-mission-action'
import { MissionSourceEnum } from '../../types/mission-types'
import { OwnerType } from '../../types/owner-type'

interface ActionHeaderActionProps {
  actionId: string
  ownerId: string
  ownerType: OwnerType
  source: MissionSourceEnum
}

export const ActionHeaderAction: React.FC<ActionHeaderActionProps> = ({ source, ownerId, actionId, ownerType }) => {
  const navigate = useNavigate()
  const { getUrl } = useGlobalRoutes()
  const mutation = useDeleteActionMutation(ownerId, ownerType)

  const handleDelete = async () => {
    await mutation.mutateAsync(actionId)
    navigate(`${getUrl(ownerType)}/${ownerId}`)
  }

  const isDeleteDisabled = () => source !== MissionSourceEnum.RAPPORTNAV

  return (
    <Stack direction="row" spacing="0.5rem">
      <Stack.Item>
        <IconButton accent={Accent.SECONDARY} size={Size.SMALL} Icon={Icon.Duplicate} disabled={true} />
      </Stack.Item>
      <Stack.Item>
        <IconButton
          accent={Accent.SECONDARY}
          size={Size.SMALL}
          Icon={Icon.Delete}
          onClick={handleDelete}
          data-testid={'deleteButton'}
          disabled={isDeleteDisabled()}
          color={isDeleteDisabled() ? undefined : THEME.color.maximumRed}
        />
      </Stack.Item>
    </Stack>
  )
}

export default ActionHeaderAction
