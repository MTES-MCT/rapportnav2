import { Accent, Icon, IconButton, Size, THEME } from '@mtes-mct/monitor-ui'
import { Stack } from 'rsuite'

interface MissionActionHeaderActionProps {}

export const MissionActionHeaderAction: React.FC<MissionActionHeaderActionProps> = () => {
  const handleDelete = () => console.log('DELETE')

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
