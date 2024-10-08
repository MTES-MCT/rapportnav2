import { useControl } from '@features/pam/mission/hooks/control/use-control.tsx'
import { FormikEffect, FormikTextarea, FormikToggle, Label, THEME } from '@mtes-mct/monitor-ui'
import { Form, Formik } from 'formik'
import { isEmpty, isEqual, isNil, isNull, omitBy, pick } from 'lodash'
import omit from 'lodash/omit'
import { FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { Panel, Stack } from 'rsuite'
import { ControlNavigation, ControlType } from '@common/types/control-types.ts'
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

  const getControlInput = (data?: ControlNavigation) => {
    let control = data
      ? (omitBy(pick(data, 'observations', 'unitHasConfirmed'), isNull) as ControlNavigationFormInput)
      : ({} as ControlNavigationFormInput)

    // hack to circumvent toggle and FormikEffect behaviour
    if ((isEmpty(control) || isNil(control.unitHasConfirmed)) && unitShouldConfirm) {
      control.unitHasConfirmed = false
    }
    return control
  }

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
    if (isEqual(value, control) || isNil(value) || isEmpty(value)) return
    await controlChanged(actionId, getControl(value))
  }

  const handleToggleControl = async (isChecked: boolean) => toggleControl(isChecked, actionId, getControl(control))
  return (
    <Panel
      header={
        <ControlTitleCheckbox
          checked={controlIsChecked}
          shouldCompleteControl={isRequired}
          controlType={ControlType.NAVIGATION}
          onChange={(isChecked: boolean) => handleToggleControl(isChecked)}
        />
      }
      // collapsible
      // defaultExpanded={controlIsEnabled(data)}
      style={{ backgroundColor: THEME.color.white, borderRadius: 0 }}
    >
      {control !== undefined && (
        <Formik initialValues={control} validateOnChange={true} enableReinitialize={true}>
          <>
            <FormikEffect onChange={handleControlChange} />
            <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
              <Stack.Item style={{ width: '100%' }}>
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
                  </Stack>
                </Form>
              </Stack.Item>

              <Stack.Item style={{ width: '100%' }}>
                <ControlInfraction
                  controlId={data?.id}
                  infractions={data?.infractions}
                  controlType={ControlType.NAVIGATION}
                />
              </Stack.Item>
            </Stack>
          </>
        </Formik>
      )}
    </Panel>
  )
}

export default ControlNavigationForm
