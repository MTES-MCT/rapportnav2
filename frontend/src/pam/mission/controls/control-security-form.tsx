import { Panel, Stack, Toggle } from 'rsuite'
import { ControlSecurity, ControlType } from '../../mission-types'
import { THEME, Icon, Button, Accent, Size, Textarea, Checkbox, Label } from '@mtes-mct/monitor-ui'
import { DELETE_CONTROL_SECURITY, MUTATION_ADD_OR_UPDATE_CONTROL_SECURITY } from '../queries'
import { useMutation } from '@apollo/client'
import omit from 'lodash/omit'
import { controlIsEnabled } from './utils'
import { useParams } from 'react-router-dom'
import ControlTitleCheckbox from './control-title-checkbox'
import ControlInfraction from './control-infraction'
import { useState } from 'react'

interface ControlSecurityFormProps {
  data?: ControlSecurity
  shouldCompleteControl?: boolean
  unitShouldConfirm?: boolean
  disableToggle?: boolean
}

const ControlSecurityForm: React.FC<ControlSecurityFormProps> = ({
  data,
  shouldCompleteControl,
  unitShouldConfirm,
  disableToggle
}) => {
  const { missionId, actionId } = useParams()

  const [showInfractions, setShowInfractions] = useState<boolean>(false)

  const [mutate, { statusData, statusLoading, statusError }] = useMutation(MUTATION_ADD_OR_UPDATE_CONTROL_SECURITY, {
    refetchQueries: ['GetMissionById']
  })

  const [deleteControl, { deleteData, deleteLoading, deleteError }] = useMutation(DELETE_CONTROL_SECURITY, {
    refetchQueries: ['GetMissionById']
  })

  const toggleControl = async (isChecked: boolean) =>
    isChecked
      ? onChange()
      : await deleteControl({
          variables: {
            actionId
          }
        })

  const onChange = (field?: string, value?: any) => {
    let updatedData = {
      ...omit(data, '__typename', 'infractions'),
      missionId: missionId,
      actionControlId: actionId,
      amountOfControls: 1,
      unitShouldConfirm: unitShouldConfirm
    }

    if (!!field && !!value) {
      updatedData = {
        ...updatedData,
        [field]: value
      }
    }
    mutate({ variables: { control: updatedData } })
  }
  return (
    <Panel
      header={
        <ControlTitleCheckbox
          controlType={ControlType.SECURITY}
          checked={!!data || shouldCompleteControl}
          shouldCompleteControl={!!shouldCompleteControl && !!!data}
          onChange={disableToggle ? undefined : (isChecked: boolean) => toggleControl(isChecked)}
        />
      }
      // collapsible
      // defaultExpanded={controlIsEnabled(data)}
      style={{ backgroundColor: THEME.color.white, borderRadius: 0 }}
    >
      <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
        {unitShouldConfirm && (
          <Stack.Item style={{ width: '100%' }}>
            <Stack direction="row" alignItems="center" spacing={'0.5rem'}>
              <Stack.Item>
                {/* TODO add Toggle component to monitor-ui */}
                <Toggle
                  checked={!!data?.unitHasConfirmed}
                  size="sm"
                  onChange={(checked: boolean) => onChange('unitHasConfirmed', checked)}
                />
              </Stack.Item>
              <Stack.Item>
                <Label>
                  <b>Contrôle confirmé par l’unité</b>
                </Label>
              </Stack.Item>
            </Stack>
          </Stack.Item>
        )}
        <Stack.Item style={{ width: '100%' }}>
          <Textarea
            name="observations"
            label="Observations (hors infraction) sur la sécurité du navire (équipements…)"
            value={data?.observations}
            onChange={(nextValue?: string) => onChange('observations', nextValue)}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <ControlInfraction
            controlId={data?.id}
            infraction={undefined}
            controlType={ControlType.SECURITY}
            showInfractions={showInfractions}
            showInfractionForm={setShowInfractions}
          />
        </Stack.Item>
      </Stack>
    </Panel>
  )
}

export default ControlSecurityForm
