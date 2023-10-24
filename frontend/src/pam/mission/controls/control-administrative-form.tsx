import { Stack } from 'rsuite'
import { ControlAdministrative } from '../../mission-types'
import { THEME, Icon, Button, Accent, Size, Textarea, MultiRadio, OptionValue } from '@mtes-mct/monitor-ui'
import { useMutation } from '@apollo/client'
import { MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE } from '../queries'
import { useEffect, useState } from 'react'
import omit from 'lodash/omit'
import { controlResultOptions } from './control-result'

interface ControlAdministrativeFormProps {
  missionId: String
  actionControlId: String
  data?: ControlAdministrative
}

const ControlAdministrativeForm: React.FC<ControlAdministrativeFormProps> = ({ missionId, actionControlId, data }) => {
  const [actionData, setActionData] = useState<ControlAdministrative>((data || {}) as unknown as ControlAdministrative)

  useEffect(() => {
    setActionData((data || {}) as any as ControlAdministrative)
  }, [data])

  const [mutate, { statusData, statusLoading, statusError }] = useMutation(
    MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE,
    {
      refetchQueries: ['GetMissionById']
    }
  )

  const onChange = (field: string, value: any) => {
    const updatedData = {
      ...omit(actionData, '__typename'),
      missionId: missionId,
      actionControlId: actionControlId,
      [field]: value
    }
    debugger
    mutate({ variables: { control: updatedData } })

    // TODO this shouldn't be like that - useState should not be used
    setActionData(updatedData)
  }

  return (
    <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <MultiRadio
          value={actionData?.compliantOperatingPermit}
          error=""
          isInline
          label="Permis de mise en exploitation (autorisation à pêcher) conforme"
          name="compliantOperatingPermit"
          onChange={(nextValue: OptionValue) => onChange('compliantOperatingPermit', nextValue)}
          options={controlResultOptions()}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <MultiRadio
          value={actionData?.upToDateNavigationPermit}
          error=""
          isInline
          label="Permis de navigation à jour"
          name="upToDateNavigationPermit"
          onChange={(nextValue: OptionValue) => onChange('upToDateNavigationPermit', nextValue)}
          options={controlResultOptions()}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <MultiRadio
          value={actionData?.compliantSecurityDocuments}
          error=""
          isInline
          label="Titres de sécurité conformes"
          name="compliantSecurityDocuments"
          onChange={(nextValue: OptionValue) => onChange('compliantSecurityDocuments', nextValue)}
          options={controlResultOptions()}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        <Textarea
          label="Observations (hors infraction) sur les pièces administratives"
          value={actionData?.observations}
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
