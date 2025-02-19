import { FormikEffect, FormikTextarea } from '@mtes-mct/monitor-ui'
import { Field, FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { FormikDateRangePicker } from '../../../common/components/ui/formik-date-range-picker'
import { MissionAction } from '../../../common/types/mission-action'
import { useMissionActionGenericDateObservation } from '../../hooks/use-mission-action-generic-date-observation'
import { ActionGenericDateObservationInput } from '../../types/action-type'

const MissionActionItemGenericDateObservation: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction, debounceTime?: number) => Promise<unknown>
}> = ({ action, onChange }) => {
  const { initValue, handleSubmit } = useMissionActionGenericDateObservation(action, onChange)

  return (
    <form style={{ width: '100%' }}>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} validateOnChange={true} enableReinitialize>
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
                <Stack.Item style={{ width: '100%' }}>
                  <FormikTextarea label="Observations" isLight={true} name="observations" data-testid="observations" />
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
