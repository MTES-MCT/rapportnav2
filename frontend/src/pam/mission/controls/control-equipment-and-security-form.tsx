import { Stack } from 'rsuite'
import { ControlEquipmentAndSecurity } from '../../mission-types'
import { THEME, Icon, Button, Accent, Size, Textarea } from '@mtes-mct/monitor-ui'

interface ControlEquipmentAndSecurityFormProps {
  data?: ControlEquipmentAndSecurity
}

const ControlEquipmentAndSecurityForm: React.FC<ControlEquipmentAndSecurityFormProps> = ({ data }) => {
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

export default ControlEquipmentAndSecurityForm
