import Text from '@common/components/ui/text'
import { FormikEffect, FormikTextarea, Label, THEME } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { MissionAction } from '../../../common/types/mission-action'
import { useMissionActionSurveillance } from '../../hooks/use-mission-action-surveillance'
import { ActionSurveillanceInput} from '../../types/action-type'
import MissionActionEnvControlPlan from '../ui/mission-action-env-control-plan'
import { MissionActionFormikDateRangePicker } from '../ui/mission-action-formik-date-range-picker'
import {simpleDateRangeValidationSchema} from "../../validation-schema/date-validation.ts";

const MissionActionItemSurveillance: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  const { initValue, handleSubmit } = useMissionActionSurveillance(action, onChange)
  return (
    <form style={{ width: '100%' }}>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} validateOnChange={true} validationSchema={simpleDateRangeValidationSchema}>
          {({ values, errors, validateForm }) => (
            <>
              <FormikEffect onChange={async (nextValue) => {
                await validateForm(values)
                await handleSubmit(nextValue as ActionSurveillanceInput, errors)
              }} />
              <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%' }}>
                <Stack.Item style={{ width: '100%' }}>
                  <Stack direction="row" spacing="0.5rem" style={{ width: '100%' }}>
                    <Stack.Item grow={1}>
                      <MissionActionFormikDateRangePicker name="dates"  isLight={true} errors={errors}/>
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <MissionActionEnvControlPlan controlPlans={values?.formattedControlPlans} />
                </Stack.Item>
                <Stack.Item>
                  <Label>Observations</Label>
                  <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                    {values?.observations ?? 'aucunes'}
                  </Text>
                </Stack.Item>

                <Stack.Item style={{ width: '100%' }}>
                  <FormikTextarea
                    isLight={true}
                    name="observationsByUnit"
                    label="Observations (unités)"
                    data-testid="observations-by-unit"
                  />
                </Stack.Item>
              </Stack>
            </>
          )}
        </Formik>
      )}
    </form>
  )
}

export default MissionActionItemSurveillance
