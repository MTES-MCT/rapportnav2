import { Panel, Stack, Toggle } from 'rsuite'
import { ControlNavigation, ControlType } from '../../mission-types'
import { THEME, Icon, Button, Accent, Size, Textarea, Checkbox, Label } from '@mtes-mct/monitor-ui'
import { MUTATION_ADD_OR_UPDATE_CONTROL_NAVIGATION } from '../queries'
import { useMutation } from '@apollo/client'
import omit from 'lodash/omit'
import { controlIsEnabled } from './utils'
import { useParams } from 'react-router-dom'
import ControlTitleCheckbox from './control-title-checkbox'

interface ControlNavigationFormProps {
  data?: ControlNavigation
  shouldCompleteControl?: boolean
  unitShouldConfirm?: boolean
}

const ControlNavigationForm: React.FC<ControlNavigationFormProps> = ({
  data,
  shouldCompleteControl,
  unitShouldConfirm
}) => {
  const { missionId, actionId } = useParams()

  const [mutate, { statusData, statusLoading, statusError }] = useMutation(MUTATION_ADD_OR_UPDATE_CONTROL_NAVIGATION, {
    refetchQueries: ['GetMissionById']
  })

  const toggleControl = (isChecked: boolean) =>
    isChecked ? onChange() : onChange('deletedAt', new Date().toISOString())

  const onChange = (field?: string, value?: any) => {
    let updatedData = {
      ...omit(data, '__typename'),
      deletedAt: undefined,
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
        <ControlTitleCheckbox
          controlType={ControlType.NAVIGATION}
          checked={!!data || shouldCompleteControl}
          shouldCompleteControl={!!shouldCompleteControl && !!!data}
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
            label="Observations (hors infraction) sur les règles de navigation"
            value={data?.observations}
            onChange={(nextValue: string) => onChange('observations', nextValue)}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <Button accent={Accent.SECONDARY} size={Size.NORMAL} Icon={Icon.Plus} isFullWidth>
            Ajouter une infraction règle de navigation
          </Button>
        </Stack.Item>
      </Stack>
    </Panel>
  )
}

export default ControlNavigationForm
