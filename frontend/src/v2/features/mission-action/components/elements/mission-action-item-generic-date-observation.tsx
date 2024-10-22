import { Action } from '@common/types/action-types'
import { FormikEffect, FormikTextarea } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { isEqual } from 'lodash'
import { FC, useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import { useDate } from '../../../common/hooks/use-date'
import { ActionDataDateObservationInput } from '../../../common/types/action-type'
import { MissionActionFormikDateRangePicker } from '../ui/mission-action-formik-date-range-picker'

type ActionDataInput = {
  dates: Date[]
} & ActionDataDateObservationInput

const MissionActionItemGenericDateObservation: FC<{ action: Action; onChange: (newAction: Action) => void }> = ({
  action,
  onChange
}) => {
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()
  const data = action?.data as unknown as ActionDataDateObservationInput
  const [initValue, setInitValue] = useState<ActionDataInput>()

  useEffect(() => {
    if (!data) return
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    const value = {
      ...data,
      dates: [startDate, endDate]
    }
    setInitValue(value)
  }, [data])

  const handleSubmit = async (value: ActionDataInput): Promise<void> => {
    if (isEqual(value, initValue)) return
    const { dates, ...newData } = value
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    const data: ActionDataDateObservationInput = { ...newData, startDateTimeUtc, endDateTimeUtc }
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
                <FormikTextarea label="Observations" isLight={true} name="observations" data-testid="observations" />
              </Stack.Item>
            </Stack>
          </>
        </Formik>
      )}
    </form>
  )
}

export default MissionActionItemGenericDateObservation
