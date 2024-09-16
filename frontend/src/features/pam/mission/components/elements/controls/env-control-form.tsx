import { useControl } from '@features/pam/mission/hooks/control/use-control.tsx'
import { FormikEffect, FormikNumberInput, FormikTextInput } from '@mtes-mct/monitor-ui'
import { Form, Formik } from 'formik'
import { isEqual, isNull, omitBy, pick } from 'lodash'
import { FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { Stack } from 'rsuite'
import {
  ControlAdministrative,
  ControlGensDeMer,
  ControlNavigation,
  ControlSecurity,
  ControlType
} from '../../../../../common/types/control-types.ts'
import ControlTitleCheckbox from '../../ui/control-title-checkbox.tsx'

export type EnvControlFormInput = {
  observations?: string
  amountOfControls?: number
}

type EnvControl = ControlAdministrative | ControlSecurity | ControlNavigation | ControlGensDeMer

export interface EnvControlFormProps {
  controlType: ControlType
  data?: EnvControl
  maxAmountOfControls?: number
  shouldCompleteControl?: boolean
}

const EnvControlForm: FC<EnvControlFormProps> = ({ controlType, data, maxAmountOfControls, shouldCompleteControl }) => {
  const { missionId, actionId } = useParams()
  const [control, setControl] = useState<EnvControlFormInput>()
  const { isRequired, toggleControl, controlIsChecked, controlEnvChanged } = useControl(
    data,
    controlType,
    shouldCompleteControl,
    3000
  )

  const getControlInput = (data?: EnvControl) =>
    data ? omitBy(pick(data, 'observations', 'amountOfControls'), isNull) : ({} as EnvControlFormInput)

  const getControl = (value?: EnvControlFormInput) => {
    if (!value) return
    return {
      missionId,
      actionControlId: actionId,
      unitHasConfirmed: undefined,
      ...value
    }
  }

  useEffect(() => {
    setControl(getControlInput(data))
  }, [data])

  const handleControlChange = async (value: EnvControlFormInput): Promise<void> => {
    if (isEqual(value, control)) return
    controlEnvChanged(actionId, getControl(value), !!value.amountOfControls)
  }

  const handleToogleControl = async (isChecked: boolean) => toggleControl(isChecked, actionId, getControl(control))

  return (
    <Stack direction="column" alignItems="flex-start" spacing={'0.5rem'} style={{ width: '100%' }}>
      <Stack.Item style={{ width: '100%' }}>
        <ControlTitleCheckbox
          controlType={controlType}
          checked={controlIsChecked}
          shouldCompleteControl={isRequired}
          onChange={(isChecked: boolean) => handleToogleControl(isChecked)}
        />
      </Stack.Item>
      <Stack.Item style={{ width: '100%' }}>
        {control !== undefined && (
          <Formik
            initialValues={control}
            validateOnChange={true}
            enableReinitialize={true}
            onSubmit={handleControlChange}
          >
            <>
              <FormikEffect onChange={handleControlChange} />
              <Form>
                <Stack direction="row" style={{ width: '100%' }} spacing={'0.5rem'}>
                  <Stack.Item style={{ width: '33%' }}>
                    <FormikNumberInput
                      min={0}
                      isLight={true}
                      label="Nb contrôles"
                      name="amountOfControls"
                      isRequired={shouldCompleteControl}
                      max={(data?.amountOfControls || 0) + (maxAmountOfControls || 0)}
                    />
                  </Stack.Item>
                  <Stack.Item style={{ width: '67%' }}>
                    <FormikTextInput isLight={true} name="observations" label="Observations (hors infraction)" />
                  </Stack.Item>
                </Stack>
              </Form>
            </>
          </Formik>
        )}
      </Stack.Item>
    </Stack>
  )
}

export default EnvControlForm
