import Text from '@common/components/ui/text.tsx'
import { useControl } from '@features/pam/mission/hooks/control/use-control.tsx'
import { FormikEffect, FormikNumberInput, FormikTextInput, THEME } from '@mtes-mct/monitor-ui'
import { Form, Formik, FormikErrors } from 'formik'
import { isEmpty, isEqual, isNull, omitBy, pick } from 'lodash'
import { FC, useEffect, useState } from 'react'
import { useParams } from 'react-router-dom'
import { Stack } from 'rsuite'
import { number, object } from 'yup'
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
  const [isError, setIsError] = useState<boolean>(false)
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

  const handleControlChange = async (
    value: EnvControlFormInput,
    errors?: FormikErrors<EnvControlFormInput>
  ): Promise<void> => {
    const isError = !isEmpty(errors)
    setIsError(isError)
    if (isError) return
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
            onSubmit={value => {
              handleControlChange(value)
            }}
            validationSchema={object().shape({
              amountOfControls: number().max(maxAmountOfControls || 0)
            })}
          >
            {({ validateForm }) => (
              <>
                <FormikEffect
                  onChange={value => validateForm(value).then(errors => handleControlChange(value, errors))}
                />
                <Form>
                  <Stack direction="column" style={{ width: '100%', justifyContent: 'start', alignItems: 'start' }}>
                    <Stack.Item style={{ width: '100%' }}>
                      <Stack
                        direction="row"
                        spacing={'0.5rem'}
                        style={{ width: '100%', alignItems: 'end', justifyContent: 'stretch' }}
                      >
                        <Stack.Item style={{ width: '33%' }}>
                          <FormikNumberInput
                            isErrorMessageHidden={true}
                            min={0}
                            isLight={true}
                            label="Nb contrôles"
                            name="amountOfControls"
                            max={maxAmountOfControls || 0}
                            isRequired={shouldCompleteControl}
                          />
                        </Stack.Item>
                        <Stack.Item>
                          <FormikTextInput
                            isLight={true}
                            name="observations"
                            label="Observations (hors infraction)"
                            style={{ width: '100%' }}
                          />
                        </Stack.Item>
                      </Stack>
                    </Stack.Item>
                    <Stack.Item style={{ width: '100%' }}>
                      <Stack.Item style={{ width: '100%', marginTop: '5px' }}>
                        {isError && (
                          <Text as={'h4'} color={THEME.color.maximumRed} fontStyle={'italic'}>
                            Attention, le chiffre renseigné ne peut pas être supérieur au nombre total de contrôles
                            effectués
                          </Text>
                        )}
                      </Stack.Item>
                    </Stack.Item>
                  </Stack>
                </Form>
              </>
            )}
          </Formik>
        )}
      </Stack.Item>
    </Stack>
  )
}

export default EnvControlForm
