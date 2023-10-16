import { Stack } from 'rsuite'
import { ControlAdministrative } from '../../mission-types'
import { THEME, Icon, Button, Accent, Size, Textarea, MultiRadio, OptionValue } from '@mtes-mct/monitor-ui'
import { CONTROL_MULTIRADIO_OPTIONS } from './utils'
import { useMutation } from '@apollo/client'
import { MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE } from '../queries'

interface ControlAdministrativeFormProps {
  missionId: String
  actionControlId: String
  data?: ControlAdministrative
}

const ControlAdministrativeForm: React.FC<ControlAdministrativeFormProps> = ({ missionId, actionControlId, data }) => {
  const [mutate, { statusData, statusLoading, statusError }] = useMutation(
    MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE,
    {
      refetchQueries: ['GetMissionById']
    }
  )

  const onChange = (field: string, value: any) => {
    const updatedData = {
      ...data,
      missionId: missionId,
      actionControlId: actionControlId,
      [field]: value
    }
    debugger
    mutate({ variables: { control: updatedData } })

    // TODO this shouldn't be like that - useState should not be used
    // setStatus(updatedData)
  }

  return (
    <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <MultiRadio
          value={data?.compliantOperatingPermit}
          error=""
          isInline
          label="Permis de mise en exploitation (autorisation à pêcher) conforme"
          name="compliantOperatingPermit"
          onChange={(nextValue: OptionValue) => onChange('compliantOperatingPermit', nextValue)}
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
          onChange={(nextValue: OptionValue) => onChange('upToDateNavigationPermit', nextValue)}
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
          onChange={(nextValue: OptionValue) => onChange('compliantSecurityDocuments', nextValue)}
          options={CONTROL_MULTIRADIO_OPTIONS}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Textarea
          label="Observations (hors infraction) sur les pièces administratives"
          value={data?.observations}
          onChange={(nextValue: string) => onChange('observations', nextValue)}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Button accent={Accent.SECONDARY} size={Size.NORMAL} Icon={Icon.Plus} isFullWidth>
          Ajouter une infraction administrative
        </Button>
      </Stack.Item>
    </Stack>
  )
}

export default ControlAdministrativeForm
