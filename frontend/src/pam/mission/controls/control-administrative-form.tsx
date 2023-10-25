import { Panel, Stack } from 'rsuite'
import { ControlAdministrative } from '../../mission-types'
import { THEME, Icon, Button, Accent, Size, Textarea, MultiRadio, OptionValue, Checkbox } from '@mtes-mct/monitor-ui'
import { useMutation } from '@apollo/client'
import { GET_MISSION_BY_ID, MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE } from '../queries'
import omit from 'lodash/omit'
import { controlResultOptions } from './control-result'
import { controlIsEnabled } from './utils'
import { useParams } from 'react-router-dom'

interface ControlAdministrativeFormProps {
  data?: ControlAdministrative
}

const ControlAdministrativeForm: React.FC<ControlAdministrativeFormProps> = ({ data }) => {
  const { missionId, actionId } = useParams()

  const [mutate, { statusData, statusLoading, statusError }] = useMutation(
    MUTATION_ADD_OR_UPDATE_CONTROL_ADMINISTRATIVE,
    {
      refetchQueries: [GET_MISSION_BY_ID]
    }
  )

  const toggleControl = (isChecked: boolean) =>
    isChecked ? onChange() : onChange('deletedAt', new Date().toISOString())

  const onChange = async (field?: string, value?: any) => {
    let updatedData = {
      ...omit(data, '__typename'),
      deletedAt: undefined,
      missionId: missionId,
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
          compliantOperatingPermit: undefined,
          upToDateNavigationPermit: undefined,
          compliantSecurityDocuments: undefined,
          observations: undefined
        }
      }
    }

    await mutate({ variables: { control: updatedData } })
  }

  return (
    <Panel
      header={
        <>
          <Checkbox
            error=""
            label="Contrôle administratif navire"
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
          <MultiRadio
            value={data?.compliantOperatingPermit}
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
            value={data?.upToDateNavigationPermit}
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
            value={data?.compliantSecurityDocuments}
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
    </Panel>
  )
}

export default ControlAdministrativeForm
