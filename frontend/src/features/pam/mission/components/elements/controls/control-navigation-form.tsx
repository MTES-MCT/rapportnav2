import { useControl } from '@features/pam/mission/hooks/control/use-control.tsx'
import { FormikEffect, FormikTextarea, FormikToggle, Label, THEME } from '@mtes-mct/monitor-ui'
import { Form, Formik } from 'formik'
import { isNull, omitBy, pick } from 'lodash'
import omit from 'lodash/omit'
import { FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { Panel, Stack } from 'rsuite'
import { ControlNavigation, ControlType } from '../../../../../common/types/control-types.ts'
import ControlTitleCheckbox from '../../ui/control-title-checkbox.tsx'
import ControlInfraction from '../infractions/infraction-for-control.tsx'

export type ControlNavigationFormInput = {
  observations?: string
  unitHasConfirmed?: boolean
}

interface ControlNavigationFormProps {
  data?: ControlNavigation
  shouldCompleteControl?: boolean
  unitShouldConfirm?: boolean
}

const ControlNavigationForm: FC<ControlNavigationFormProps> = ({ data, shouldCompleteControl, unitShouldConfirm }) => {
  const { missionId, actionId } = useParams()
  const [control, setControl] = useState<ControlNavigationFormInput>()
  const { isRequired, controlChanged, toggleControl, controlIsChecked } = useControl(
    data,
    ControlType.NAVIGATION,
    shouldCompleteControl
  )

  const getControlInput = (data?: ControlNavigationFormInput) =>
    data ? omitBy(pick(data, 'observations', 'unitHasConfirmed'), isNull) : ({} as ControlNavigationFormInput)

  useEffect(() => {
    setControl(getControlInput(data))
  }, [data])

  const getControl = (value?: ControlNavigationFormInput) => {
    if (!value) return
    return {
      ...omit(data, 'infractions'),
      missionId,
      unitShouldConfirm,
      amountOfControls: 1,
      actionControlId: actionId,
      ...value
    }
  }

  const handleControlChange = async (value: ControlNavigationFormInput): Promise<void> => {
    if (value === control) return
    controlChanged(actionId, getControl(value))
  }

  const handleToogleControl = async (isChecked: boolean) => toggleControl(isChecked, actionId, getControl(control))
  return (
    <Panel
      header={
        <ControlTitleCheckbox
          checked={controlIsChecked}
          shouldCompleteControl={isRequired}
          controlType={ControlType.NAVIGATION}
          onChange={(isChecked: boolean) => handleToogleControl(isChecked)}
        />
      }
      // collapsible
      // defaultExpanded={controlIsEnabled(data)}
      style={{ backgroundColor: THEME.color.white, borderRadius: 0 }}
    >
      {control !== undefined && (
        <Formik
          initialValues={control}
          onSubmit={handleControlChange}
          validateOnChange={true}
          enableReinitialize={true}
        >
          <>
            <FormikEffect onChange={handleControlChange} />
            <Form>
              <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
                {unitShouldConfirm && (
                  <Stack.Item style={{ width: '100%' }}>
                    <Stack direction="row" alignItems="center" spacing={'0.5rem'}>
                      <Stack.Item>
                        <FormikToggle label="" size="sm" name="unitHasConfirmed" />
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
                  <FormikTextarea
                    name="observations"
                    label="Observations (hors infraction) sur les règles de navigation"
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
            </Form>
          </>
        </Formik>
      )}
    </Panel>
  )
}

export default ControlNavigationForm
