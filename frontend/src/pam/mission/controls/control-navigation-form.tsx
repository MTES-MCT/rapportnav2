import { Stack } from 'rsuite'
import { ControlNavigation } from '../../mission-types'
import { THEME, Icon, Button, Accent, Size, Textarea } from '@mtes-mct/monitor-ui'

interface ControlNavigationFormProps {
  data?: ControlNavigation
}

const ControlNavigationForm: React.FC<ControlNavigationFormProps> = ({ data }) => {
  return (
    <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Textarea label="Observations (hors infraction) sur les règles de navigation" value={data?.observations} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Button accent={Accent.SECONDARY} size={Size.NORMAL} Icon={Icon.Plus} isFullWidth>
          Ajouter une infraction règle de navigation
        </Button>
      </Stack.Item>
    </Stack>
  )
}

export default ControlNavigationForm