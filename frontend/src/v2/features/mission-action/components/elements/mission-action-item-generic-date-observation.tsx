import { FormikEffect, FormikTextarea } from '@mtes-mct/monitor-ui'
import { Formik} from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { MissionAction } from '../../../common/types/mission-action'
import { useMissionActionGenericDateObservation } from '../../hooks/use-mission-action-generic-date-observation'
import { ActionGenericDateObservationInput } from '../../types/action-type'
import { MissionActionFormikDateRangePicker } from '../ui/mission-action-formik-date-range-picker'
import {simpleDateRangeValidationSchema} from "../../validation-schema/date-validation.ts";

const MissionActionItemGenericDateObservation: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction, debounceTime?: number) => Promise<unknown>
}> = ({ action, onChange }) => {
  const { initValue, handleSubmit } = useMissionActionGenericDateObservation(action, onChange)

  return (
    <form style={{ width: '100%' }}>
      {initValue && (
        <Formik //
           initialValues={initValue}
           onSubmit={handleSubmit}
           validationSchema={simpleDateRangeValidationSchema}
           validateOnChange={true}>
          {({ values, errors, validateForm }) => (
          <>
            <FormikEffect onChange={async (nextValue) => {
              await validateForm(values)
              await handleSubmit(nextValue as ActionGenericDateObservationInput, errors)
            }} />
            <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%' }}>
              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="row" spacing="0.5rem" style={{ width: '100%' }}>
                  <Stack.Item grow={1}>
                    <MissionActionFormikDateRangePicker name="dates" isLight={true} errors={errors} />
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
