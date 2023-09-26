import { Stack } from 'rsuite'
import { ControlVesselAdministrative } from '../../mission-types'
import { THEME, Icon, Button, Accent, Size, Textarea, MultiRadio } from '@mtes-mct/monitor-ui'
import { CONTROL_MULTIRADIO_OPTIONS } from './utils'

interface ControlVesselAdministrativeFormProps {
  data?: ControlVesselAdministrative
}

const ControlVesselAdministrativeForm: React.FC<ControlVesselAdministrativeFormProps> = ({ data }) => {
  return (
    <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <MultiRadio
          value={data?.compliantOperatingPermit}
          error=""
          isInline
          label="Permis de mise en exploitation (autorisation à pêcher) conforme"
          name="compliantOperatingPermit"
          onChange={function noRefCheck() {}}
          options={CONTROL_MULTIRADIO_OPTIONS}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <MultiRadio
          value={data?.upToDateNavigationPermit}
          error=""
          isInline
          label="Permis de navigation à jour"
          name="upToDateNavigationPermit"
          onChange={function noRefCheck() {}}
          options={CONTROL_MULTIRADIO_OPTIONS}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <MultiRadio
          value={data?.compliantSecurityDocuments}
          error=""
          isInline
          label="Titres de sécurité conformes"
          name="compliantSecurityDocuments"
          onChange={function noRefCheck() {}}
          options={CONTROL_MULTIRADIO_OPTIONS}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Textarea label="Observations (hors infraction) sur les pièces administratives" value={data?.observations} />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Button accent={Accent.SECONDARY} size={Size.NORMAL} Icon={Icon.Plus} isFullWidth>
          Ajouter une infraction administrative
        </Button>
      </Stack.Item>
    </Stack>
  )
}

export default ControlVesselAdministrativeForm
