import { Panel, Stack, Toggle } from 'rsuite'
import { ControlNavigation, ControlType } from '../../../../../common/types/control-types.ts'
import { Label, Textarea, THEME } from '@mtes-mct/monitor-ui'
import omit from 'lodash/omit'
import { useParams } from 'react-router-dom'
import ControlTitleCheckbox from '../../ui/control-title-checkbox.tsx'
import ControlInfraction from '../infractions/infraction-for-control.tsx'
import { FC, useEffect, useState } from 'react'
import useAddOrUpdateControl from '../../../hooks/use-add-update-control.tsx'
import useDeleteControl from '../../../hooks/use-delete-control.tsx'

interface ControlNavigationFormProps {
  data?: ControlNavigation
  shouldCompleteControl?: boolean
  unitShouldConfirm?: boolean
}

const ControlNavigationForm: FC<ControlNavigationFormProps> = ({ data, shouldCompleteControl, unitShouldConfirm }) => {
  const { missionId, actionId } = useParams()

  const [observationsValue, setObservationsValue] = useState<string | undefined>(data?.observations)

  const handleObservationsChange = (nextValue?: string) => {
    setObservationsValue(nextValue)
  }

  useEffect(() => {
    setObservationsValue(data?.observations)
  }, [data])

  const handleObservationsBlur = async () => {
    if (observationsValue !== data?.observations) {
      await onChange('observations', observationsValue)
    }
  }

  const [mutateControl, { loading: mutationLoading }] = useAddOrUpdateControl({ controlType: ControlType.NAVIGATION })
  const [deleteControl] = useDeleteControl({ controlType: ControlType.NAVIGATION })

  const toggleControl = async (isChecked: boolean) =>
    isChecked
      ? onChange()
      : await deleteControl({
          variables: {
            actionId
          }
        })

  const onChange = async (field?: string, value?: any) => {
    let updatedData = {
      ...omit(data, '__typename', 'infractions'),
      id: data?.id,
      missionId: missionId,
      actionControlId: actionId,
      amountOfControls: 1,
      unitShouldConfirm: unitShouldConfirm
    }

    if (!!field && value !== undefined) {
      updatedData = {
        ...updatedData,
        [field]: value
      }
    }
    await mutateControl({ variables: { control: updatedData } })
  }
  return (
    <Panel
      header={
        <ControlTitleCheckbox
          controlType={ControlType.NAVIGATION}
          checked={!!data || shouldCompleteControl}
          disabled={mutationLoading}
          shouldCompleteControl={!!shouldCompleteControl && !!!data}
          onChange={(isChecked: boolean) => toggleControl(isChecked)}
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
                  disabled={mutationLoading}
                />
              </Stack.Item>
              <Stack.Item alignSelf="flex-end">
                <Label style={{ marginBottom: 0 }}>
                  <b>Contrôle confirmé par l’unité</b>
                </Label>
              </Stack.Item>
            </Stack>
          </Stack.Item>
        )}
        <Stack.Item style={{ width: '100%' }}>
          <Textarea
            name="observations"
            label="Observations (hors infraction) sur les règles de navigation"
            value={observationsValue}
            onChange={handleObservationsChange}
            onBlur={handleObservationsBlur}
            disabled={mutationLoading}
          />
        </Stack.Item>
        <Stack.Item style={{ width: '100%' }}>
          <ControlInfraction
            controlId={data?.id}
            infractions={data?.infractions}
            controlType={ControlType.NAVIGATION}
          />
        </Stack.Item>
      </Stack>
    </Panel>
  )
}

export default ControlNavigationForm
