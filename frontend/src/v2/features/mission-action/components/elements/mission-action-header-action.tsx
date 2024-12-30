import { Accent, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { useNavigate } from 'react-router-dom'
import { Stack } from 'rsuite'
import useDeleteActionMutation from '../../../common/services/use-delete-mission-action'

interface MissionActionHeaderActionProps {
  actionId: string
  missionId: number
}

export const MissionActionHeaderAction: React.FC<MissionActionHeaderActionProps> = ({ missionId, actionId }) => {
  const navigate = useNavigate()
  const mutation = useDeleteActionMutation(missionId)
  const handleDelete = async () => {
    await mutation.mutateAsync(actionId)
    navigate(`/v2/pam/missions/${missionId}`)
  }

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
          color={THEME.color.maximumRed}
          onClick={handleDelete}
          data-testid={'deleteButton'}
        />
      </Stack.Item>
    </Stack>
  )
}

export default MissionActionHeaderAction
