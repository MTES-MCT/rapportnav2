import { useControl } from '@features/pam/mission/hooks/control/use-control.tsx'
import { FormikEffect, FormikTextarea, FormikToggle, Label, THEME } from '@mtes-mct/monitor-ui'
import { Form, Formik } from 'formik'
import { isEmpty, isEqual, isNil, isNull, omitBy, pick } from 'lodash'
import omit from 'lodash/omit'
import { FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { Panel, Stack } from 'rsuite'
import { ControlSecurity, ControlType } from '@common/types/control-types.ts'
import ControlTitleCheckbox from '../../ui/control-title-checkbox.tsx'
import ControlInfraction from '../infractions/infraction-for-control.tsx'
import { ControlNavigationFormInput } from '@features/pam/mission/components/elements/controls/control-navigation-form.tsx'

export type ControlSecurityFormInput = {
  observations?: string
  unitHasConfirmed?: boolean
}

interface ControlSecurityFormProps {
  data?: ControlSecurity
  shouldCompleteControl?: boolean
  unitShouldConfirm?: boolean
}

const ControlSecurityForm: FC<ControlSecurityFormProps> = ({ data, shouldCompleteControl, unitShouldConfirm }) => {
  const { missionId, actionId } = useParams()
  const [control, setControl] = useState<ControlSecurityFormInput>()
  const { isRequired, controlChanged, toggleControl, controlIsChecked } = useControl(
    data,
    ControlType.SECURITY,
    shouldCompleteControl
  )

  const getControlInput = (data?: ControlSecurity) => {
    let control = data
      ? omitBy(pick(data, 'observations', 'unitHasConfirmed'), isNull)
      : ({} as ControlSecurityFormInput)

    // hack to circumvent toggle and FormikEffect behaviour
    if ((isEmpty(control) || isNil(control.unitHasConfirmed)) && unitShouldConfirm) {
      control.unitHasConfirmed = false
    }
    return control
  }

  useEffect(() => {
    setControl(getControlInput(data))
  }, [data])

  const getControl = (value?: ControlSecurityFormInput) => {
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

  const handleControlChange = async (value: ControlSecurityFormInput): Promise<void> => {
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
          controlType={ControlType.SECURITY}
          onChange={(isChecked: boolean) => handleToggleControl(isChecked)}
        />
      }
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
                    label="Observations (hors infraction) sur la sécurité du navire (équipements…)"
                  />
                </Stack.Item>

                <Stack.Item style={{ width: '100%' }}>
                  <ControlInfraction
                    controlId={data?.id}
                    infractions={data?.infractions}
                    controlType={ControlType.SECURITY}
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

export default ControlSecurityForm
