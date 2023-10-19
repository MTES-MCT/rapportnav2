import { Stack } from 'rsuite'
import { ControlSecurity } from '../../mission-types'
import { THEME, Icon, Button, Accent, Size, Textarea } from '@mtes-mct/monitor-ui'
import { MUTATION_ADD_OR_UPDATE_CONTROL_SECURITY } from '../queries'
import { useMutation } from '@apollo/client'
import { useEffect, useState } from 'react'
import omit from 'lodash/omit'

interface ControlSecurityFormProps {
  missionId: String
  actionControlId: String
  data?: ControlSecurity
}

const ControlSecurityForm: React.FC<ControlSecurityFormProps> = ({ missionId, actionControlId, data }) => {
  const [actionData, setActionData] = useState<ControlSecurity>((data || {}) as unknown as ControlSecurity)

  useEffect(() => {
    setActionData((data || {}) as any as ControlSecurity)
  }, [data])

  const [mutate, { statusData, statusLoading, statusError }] = useMutation(MUTATION_ADD_OR_UPDATE_CONTROL_SECURITY, {
    refetchQueries: ['GetMissionById']
  })

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
        <Textarea
          label="Observations (hors infraction) sur la sécurité du navire (équipements…)"
          value={actionData?.observations}
          onChange={(nextValue: string) => onChange('observations', nextValue)}
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
