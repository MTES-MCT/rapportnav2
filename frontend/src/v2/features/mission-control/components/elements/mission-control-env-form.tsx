import { ControlType } from '@common/types/control-types.ts'
import { FormikEffect, FormikNumberInput, FormikTextInput } from '@mtes-mct/monitor-ui'
import { FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { EnvControl, EnvControlInput, useEnvControl } from '../../hooks/use-control-env.tsx'
import MissionControlEnvError from '../ui/mission-control-env-error.tsx'
import { MissionControlFormikCheckBoxTitle } from '../ui/mission-control-title-checkbox.tsx'

export interface MissionControlEnvFormProps {
  name: string
  controlType: ControlType
  maxAmountOfControls?: number
  shouldCompleteControl?: boolean
  fieldFormik: FieldProps<EnvControl>
}

const MissionControlEnvForm: FC<MissionControlEnvFormProps> = ({
  name,
  controlType,
  fieldFormik,
  maxAmountOfControls,
  shouldCompleteControl
}) => {
  const { initValue, controlTypeLabel, isError, handleSubmit, getValidationSchema } = useEnvControl(
    name,
    fieldFormik,
    controlType
  )

  return (
    <>
      {initValue && (
        <Formik
          validateOnChange={true}
          initialValues={initValue}
          enableReinitialize={true}
          onSubmit={value => {
            handleSubmit(value)
          }}
          validationSchema={getValidationSchema(maxAmountOfControls)}
        >
          {({ validateForm }) => (
            <>
              <FormikEffect
                onChange={value => validateForm(value).then(errors => handleSubmit(value as EnvControlInput, errors))}
              />
              <Stack direction="column" alignItems="flex-start" spacing={'0.5rem'} style={{ width: '100%' }}>
                <Stack.Item style={{ width: '100%' }}>
                  <MissionControlFormikCheckBoxTitle
                    shouldComplete={true}
                    text={controlTypeLabel}
                    name={`unitHasConfirmed`}
                  />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <Stack direction="column" style={{ width: '100%', justifyContent: 'start', alignItems: 'start' }}>
                    <Stack.Item style={{ width: '100%' }}>
                      <Stack
                        direction="row"
                        spacing={'0.5rem'}
                        style={{ width: '100%', alignItems: 'end', justifyContent: 'stretch' }}
                      >
                        <Stack.Item style={{ width: '33%' }}>
                          <FormikNumberInput
                            min={0}
                            isLight={true}
                            label="Nb contrôles"
                            name="amountOfControls"
                            isErrorMessageHidden={true}
                            max={maxAmountOfControls || 0}
                            isRequired={shouldCompleteControl}
                          />
                        </Stack.Item>
                        <Stack.Item>
                          <FormikTextInput
                            isLight={true}
                            name="observations"
                            style={{ width: '100%' }}
                            label="Observations (hors infraction)"
                          />
                        </Stack.Item>
                      </Stack>
                    </Stack.Item>
                    <Stack.Item style={{ width: '100%' }}>{isError && <MissionControlEnvError />}</Stack.Item>
                  </Stack>
                </Stack.Item>
              </Stack>
            </>
          )}
        </Formik>
      )}
    </>
  )
}

export default MissionControlEnvForm
