import Text from '@common/components/ui/text'
import { Action } from '@common/types/action-types'
import { EnvActionSurveillance } from '@common/types/env-mission-types'
import { FormikEffect, FormikTextarea, Label, THEME } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { isEqual } from 'lodash'
import { FC, useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import { useDate } from '../../../common/hooks/use-date'
import MissionActionEnvControlPlan from '../ui/mission-action-env-control-plan'
import { MissionActionFormikDateRangePicker } from '../ui/mission-action-formik-date-range-picker'

type ActionDataInput = {
  dates: Date[]
} & EnvActionSurveillance

const MissionActionItemSurveillance: FC<{ action: Action; onChange: (newAction: Action) => void }> = ({
  action,
  onChange
}) => {
  const data = action?.data as unknown as EnvActionSurveillance
  const [initValue, setInitValue] = useState<ActionDataInput>()
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  useEffect(() => {
    if (!data) return
    const endDate = preprocessDateForPicker(data?.actionEndDateTimeUtc)
    const startDate = preprocessDateForPicker(data.actionStartDateTimeUtc)
    const value = {
      ...data,
      dates: [startDate, endDate]
    }
    setInitValue(value)
  }, [data])

  const handleSubmit = async (value: ActionDataInput): Promise<void> => {
    if (isEqual(value, initValue)) return
    const { dates, ...newData } = value
    const actionEndDateTimeUtc = postprocessDateFromPicker(dates[1])
    const actionStartDateTimeUtc = postprocessDateFromPicker(dates[0])
    const data: EnvActionSurveillance = { ...newData, actionStartDateTimeUtc, actionEndDateTimeUtc }
    setInitValue(value)
    onChange({ ...action, data: [data] })
  }
  return (
    <form style={{ width: '100%' }}>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} validateOnChange={true}>
          <>
            <FormikEffect onChange={nextValues => handleSubmit(nextValues as ActionDataInput)} />
            <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%' }}>
              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="row" spacing="0.5rem" style={{ width: '100%' }}>
                  <Stack.Item grow={1}>
                    <MissionActionFormikDateRangePicker name="dates" />
                  </Stack.Item>
                </Stack>
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <MissionActionEnvControlPlan controlPlans={data?.formattedControlPlans} />
              </Stack.Item>

              <Stack.Item>
                <Label>Observations</Label>
                <Text as="h3" weight="medium" color={THEME.color.gunMetal}>
                  {data?.observations ?? 'aucunes'}
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
        </Formik>
      )}
    </form>
  )
}

export default MissionActionItemSurveillance
