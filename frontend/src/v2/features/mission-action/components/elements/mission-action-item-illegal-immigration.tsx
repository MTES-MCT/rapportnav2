import { Action, ActionIllegalImmigration } from '@common/types/action-types'
import { FormikEffect, FormikTextarea } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { isEqual } from 'lodash'
import { FC, useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import { useDate } from '../../../common/hooks/use-date'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd'
import { MissionActionFormikDateRangePicker } from '../ui/mission-action-formik-date-range-picker'
import { MissionActionFormikNumberInput } from '../ui/mission-action-formik-number-input'

type ActionDataInput = {
  dates: Date[]
  geoCoords: (number | undefined)[]
} & ActionIllegalImmigration

const MissionActionItemIllegalImmigration: FC<{ action: Action; onChange: (newAction: Action) => void }> = ({
  action,
  onChange
}) => {
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()
  const data = action?.data as unknown as ActionIllegalImmigration
  const [initValue, setInitValue] = useState<ActionDataInput>()

  useEffect(() => {
    if (!data) return
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)

    const value = {
      ...data,
      dates: [startDate, endDate],
      geoCoords: [data.latitude, data.longitude]
    }
    setInitValue(value)
  }, [data])

  const handleSubmit = async (value: ActionDataInput): Promise<void> => {
    if (isEqual(value, initValue)) return
    const { dates, geoCoords, ...newData } = value
    const latitude = geoCoords[0]
    const longitude = geoCoords[1]
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    const data: ActionIllegalImmigration = { ...newData, startDateTimeUtc, endDateTimeUtc, longitude, latitude }
    setInitValue(value)
    onChange({ ...action, data: [data] })
  }

  return (
    <form style={{ width: '100%' }} data-testid={'action-nautical-event-form'}>
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
                <MissionActionFormikCoordinateInputDMD name={'geoCoords'} />
              </Stack.Item>
              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="column" alignItems="flex-start" spacing="1rem" style={{ width: '100%' }}>
                  <Stack.Item style={{ width: '100%' }}>
                    <MissionActionFormikNumberInput
                      name="nbOfInterceptedVessels"
                      role="nbOfInterceptedVessels"
                      label="Nb de navires/embarcations interceptées"
                      //error={getError(actionData, isMissionFinished, 'nbOfInterceptedVessels')}
                    />
                  </Stack.Item>
                  <Stack.Item style={{ width: '100%' }}>
                    <Stack direction={'row'} spacing={'1rem'}>
                      <Stack.Item style={{ flex: 1 }}>
                        <MissionActionFormikNumberInput
                          name="nbOfInterceptedMigrants"
                          role="nbOfInterceptedMigrants"
                          label="Nb de migrants interceptés"
                          //error={getError(actionData, isMissionFinished, 'nbOfInterceptedMigrants')}
                        />
                      </Stack.Item>
                      <Stack.Item style={{ flex: 1 }}>
                        <MissionActionFormikNumberInput
                          name="nbOfSuspectedSmugglers"
                          role="nbOfSuspectedSmugglers"
                          label="Nb de passeurs présumés"
                          //error={getError(actionData, isMissionFinished, 'nbOfSuspectedSmugglers')}
                        />
                      </Stack.Item>
                    </Stack>
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

export default MissionActionItemIllegalImmigration
