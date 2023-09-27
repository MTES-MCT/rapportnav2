import { Stack } from 'rsuite'
import { ControlSecurity } from '../../mission-types'
import { THEME, Icon, Button, Accent, Size, Textarea } from '@mtes-mct/monitor-ui'

interface ControlSecurityFormProps {
  data?: ControlSecurity
}

const ControlSecurityForm: React.FC<ControlSecurityFormProps> = ({ data }) => {
  return (
    <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <Textarea
          label="Observations (hors infraction) sur la sécurité du navire (équipements…)"
          value={data?.observations}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Button accent={Accent.SECONDARY} size={Size.NORMAL} Icon={Icon.Plus} isFullWidth>
          Ajouter une infraction sécurité
        </Button>
      </Stack.Item>
    </Stack>
  )
}

export default ControlSecurityForm
