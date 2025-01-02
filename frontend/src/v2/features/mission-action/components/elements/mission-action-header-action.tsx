import { MissionSourceEnum } from '@common/types/env-mission-types'
import { Accent, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { useNavigate } from 'react-router-dom'
import { Stack } from 'rsuite'
import useDeleteActionMutation from '../../../common/services/use-delete-mission-action'

interface MissionActionHeaderActionProps {
  actionId: string
  missionId: number
  source: MissionSourceEnum
}

export const MissionActionHeaderAction: React.FC<MissionActionHeaderActionProps> = ({
  source,
  missionId,
  actionId
}) => {
  const navigate = useNavigate()
  const mutation = useDeleteActionMutation(missionId)

  const handleDelete = async () => {
    await mutation.mutateAsync(actionId)
    navigate(`/v2/pam/missions/${missionId}`)
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

export default MissionActionHeaderAction
