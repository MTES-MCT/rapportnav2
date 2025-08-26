import { FormikCheckbox, FormikEffect, FormikTextarea, THEME } from '@mtes-mct/monitor-ui'
import { Field, FieldProps, Formik } from 'formik'
import { FC, JSX } from 'react'
import { Divider, Stack } from 'rsuite'
import { ObjectShape } from 'yup'
import { FormikDateRangePicker } from '../../../common/components/ui/formik-date-range-picker'
import { MissionAction } from '../../../common/types/mission-action'
import { useMissionActionGenericDateObservation } from '../../hooks/use-mission-action-generic-date-observation'
import { ActionGenericDateObservationInput } from '../../types/action-type'

const MissionActionItemGenericDateObservation: FC<{
  action: MissionAction
  children?: JSX.Element
  showDivingCheckBox?: boolean
  schema?: ObjectShape
  onChange: (newAction: MissionAction, debounceTime?: number) => Promise<unknown>
}> = ({ action, onChange, children, schema, showDivingCheckBox }) => {
  const { initValue, handleSubmit, validationSchema, errors } = useMissionActionGenericDateObservation(
    action,
    onChange,
    schema
  )

  return (
    <form style={{ width: '100%' }}>
      {initValue && (
        <Formik
          initialValues={initValue}
          onSubmit={handleSubmit}
          validateOnChange={true}
          enableReinitialize
          validationSchema={validationSchema}
          initialErrors={errors}
        >
          {() => (
            <>
              <FormikEffect onChange={nextValue => handleSubmit(nextValue as ActionGenericDateObservationInput)} />
              <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%' }}>
                <Stack.Item style={{ width: '100%' }}>
                  <Stack direction="row" spacing="0.5rem" style={{ width: '100%' }}>
                    <Stack.Item grow={1}>
                      <Field name="dates">
                        {(field: FieldProps<Date[]>) => (
                          <FormikDateRangePicker label="" name="dates" isLight={true} fieldFormik={field} />
                        )}
                      </Field>
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
                {children}
                <Stack.Item style={{ width: '100%' }}>
                  <FormikTextarea label="Observations" isLight={true} name="observations" data-testid="observations" />
                  {showDivingCheckBox && (
                    <Stack.Item style={{ width: '100%' }}>
                      <Divider style={{ backgroundColor: THEME.color.charcoal, marginBottom: 4 }} />
                      <FormikCheckbox isLight name="hasDivingDuringOperation" label="Plongée au cours de l'opération" />
                    </Stack.Item>
                  )}
                </Stack.Item>
              </Stack>
            </>
          )}
        </Formik>
      )}
    </form>
  )
}

export default MissionActionItemGenericDateObservation
