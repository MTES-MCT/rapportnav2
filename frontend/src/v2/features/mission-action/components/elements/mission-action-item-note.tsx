import { Action, ActionFreeNote } from '@common/types/action-types'
import { FormikDatePicker, FormikEffect, FormikTextarea } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { isEqual } from 'lodash'
import { FC, useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import { useDate } from '../../../common/hooks/use-date'

type ActionDataInput = {
  id: string
  date: Date
  observations?: string
}

const MissionActionItemNote: FC<{ action: Action; onChange: (newAction: Action) => void }> = ({ action, onChange }) => {
  const data = action?.data as unknown as ActionFreeNote
  const [initValue, setInitValue] = useState<ActionDataInput>()
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  useEffect(() => {
    if (!data) return
    const value = {
      ...data,
      date: preprocessDateForPicker(data.startDateTimeUtc)
    }
    setInitValue(value)
  }, [data])

  const handleSubmit = async (value: ActionDataInput): Promise<void> => {
    if (isEqual(value, initValue)) return
    const { date, ...newData } = value
    const startDateTimeUtc = postprocessDateFromPicker(date)
    const data: ActionFreeNote = { ...newData, startDateTimeUtc }
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
                    <FormikDatePicker
                      name="date"
                      isLight={true}
                      withTime={true}
                      isRequired={true}
                      isCompact={false}
                      label="Date et heure"
                    />
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

export default MissionActionItemNote
