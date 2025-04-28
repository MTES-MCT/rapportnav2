import { ControlType } from '@common/types/control-types.ts'
import { FormikEffect, FormikNumberInput, FormikTextInput } from '@mtes-mct/monitor-ui'
import { FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { Control } from '../../../common/types/target-types.ts'
import { ControlEnvInput, useEnvControl } from '../../hooks/use-control-env.tsx'
import MissionControlEnvError from '../ui/mission-control-env-error.tsx'
import { MissionControlTitle } from '../ui/mission-control-title.tsx'

export interface MissionControlEnvFormProps {
  name: string
  isToComplete?: boolean
  controlType: ControlType
  maxAmountOfControls?: number
  fieldFormik: FieldProps<Control>
}

const MissionControlEnvForm: FC<MissionControlEnvFormProps> = ({
  name,
  controlType,
  fieldFormik,
  isToComplete,
  maxAmountOfControls
}) => {
  const { initValue, controlTypeLabel, isError, handleSubmit, validationSchema } = useEnvControl(
    name,
    fieldFormik,
    controlType,
    maxAmountOfControls
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
          validationSchema={validationSchema}
        >
          {({ validateForm }) => (
            <>
              <FormikEffect
                onChange={value => validateForm(value).then(errors => handleSubmit(value as ControlEnvInput, errors))}
              />
              <Stack direction="column" alignItems="flex-start" spacing={'0.5rem'} style={{ width: '100%' }}>
                <Stack.Item style={{ width: '100%' }}>
                  <MissionControlTitle isToComplete={isToComplete} text={controlTypeLabel} />
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
                            isRequired={isToComplete}
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
