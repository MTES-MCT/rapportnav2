import { Stack } from 'rsuite'
import { ControlNavigation } from '../../mission-types'
import { THEME, Icon, Button, Accent, Size, Textarea } from '@mtes-mct/monitor-ui'

interface ControlNavigationFormProps {
  missionId: String
  actionControlId: String
  data?: ControlNavigation
}

const ControlNavigationForm: React.FC<ControlNavigationFormProps> = ({ missionId, actionControlId, data }) => {
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
