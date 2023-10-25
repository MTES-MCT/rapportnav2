import { Panel, Stack } from 'rsuite'
import { ControlSecurity } from '../../mission-types'
import { THEME, Icon, Button, Accent, Size, Textarea, Checkbox } from '@mtes-mct/monitor-ui'
import { MUTATION_ADD_OR_UPDATE_CONTROL_SECURITY } from '../queries'
import { useMutation } from '@apollo/client'
import omit from 'lodash/omit'
import { controlIsEnabled } from './utils'
import { useParams } from 'react-router-dom'

interface ControlSecurityFormProps {
  data?: ControlSecurity
}

const ControlSecurityForm: React.FC<ControlSecurityFormProps> = ({ data }) => {
  const { missionId, actionId } = useParams()

  const [mutate, { statusData, statusLoading, statusError }] = useMutation(MUTATION_ADD_OR_UPDATE_CONTROL_SECURITY, {
    refetchQueries: ['GetMissionById']
  })

  const toggleControl = (isChecked: boolean) =>
    isChecked ? onChange() : onChange('deletedAt', new Date().toISOString())

  const onChange = (field?: string, value?: any) => {
    let updatedData = {
      ...omit(data, '__typename'),
      missionId: missionId,
      deletedAt: undefined,
      actionControlId: actionId
    }

    if (!!field && !!value) {
      updatedData = {
        ...updatedData,
        [field]: value
      }
      // TODO - data reset should not be handle by frontend
      if (field === 'deletedAt') {
        updatedData = {
          ...updatedData,
          observations: undefined
        }
      }
    }
    mutate({ variables: { control: updatedData } })
  }
  return (
    <Panel
      header={
        <>
          <Checkbox
            error=""
            label="Equipements et respect des normes de sécurité"
            name="control"
            checked={controlIsEnabled(data)}
            onChange={(isChecked: boolean) => toggleControl(isChecked)}
          />
        </>
      }
      // collapsible
      // defaultExpanded={controlIsEnabled(data)}
      style={{ backgroundColor: THEME.color.white, borderRadius: 0 }}
    >
      <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
        <Stack.Item style={{ width: '100%' }}>
          <Textarea
            label="Observations (hors infraction) sur la sécurité du navire (équipements…)"
            value={data?.observations}
            onChange={(nextValue: string) => onChange('observations', nextValue)}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Button accent={Accent.SECONDARY} size={Size.NORMAL} Icon={Icon.Plus} isFullWidth>
            Ajouter une infraction sécurité
          </Button>
        </Stack.Item>
      </Stack>
    </Panel>
  )
}

export default ControlSecurityForm
