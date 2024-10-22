import { Action, ActionAntiPollution } from '@common/types/action-types'
import { FormikCheckbox, FormikEffect, FormikTextarea, Icon, THEME } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { isEqual, mapValues, pick } from 'lodash'
import { FC, useEffect, useState } from 'react'
import { Stack } from 'rsuite'
import { StyledFormikToggle } from '../../../common/components/ui/formik-styled-toogle'
import { useDate } from '../../../common/hooks/use-date'
import MissionActionAntiPollutionWarning from '../ui/mission-action-anti-pollution-warning'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd'
import { MissionActionFormikDateRangePicker } from '../ui/mission-action-formik-date-range-picker'

type ActionDataInput = {
  dates: Date[]
  geoCoords: (number | undefined)[]
} & ActionAntiPollution

const MissionActionItemAntiPollution: FC<{ action: Action; onChange: (newAction: Action) => void }> = ({
  action,
  onChange
}) => {
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()
  const data = action?.data as unknown as ActionAntiPollution
  const [initValue, setInitValue] = useState<ActionDataInput>()

  useEffect(() => {
    if (!data) return
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)

    const booleans = mapValues(
      pick(
        data,
        'detectedPollution',
        'diversionCarriedOut',
        'isAntiPolDeviceDeployed',
        'isSimpleBrewingOperationDone',
        'pollutionObservedByAuthorizedAgent'
      ),
      o => !!o
    )
    const value = {
      ...data,
      dates: [startDate, endDate],
      geoCoords: [data.latitude, data.longitude],
      ...booleans
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
    const data: ActionAntiPollution = { ...newData, startDateTimeUtc, endDateTimeUtc, longitude, latitude }
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
                  <Stack.Item alignSelf="baseline">
                    <Icon.Info color={THEME.color.charcoal} size={20} />
                  </Stack.Item>
                  <Stack.Item>
                    <MissionActionAntiPollutionWarning />
                  </Stack.Item>
                </Stack>
              </Stack.Item>

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
                <Stack direction={'column'} spacing={'0.5rem'}>
                  <Stack.Item style={{ width: '100%', display: 'flex', flexDirection: 'row', alignItems: 'end' }}>
                    <StyledFormikToggle
                      name="isSimpleBrewingOperationDone"
                      label="Opération de brassage simple effectuée"
                      data-testid="action-antipol-simple-brewing-operation"
                    />
                  </Stack.Item>
                  <Stack.Item style={{ width: '100%' }}>
                    <StyledFormikToggle
                      name="isAntiPolDeviceDeployed"
                      data-testid="action-antipol-device-deployed"
                      label="Mise en place d'un dispositif ANTIPOL (dispersant, barrage, etc...)"
                    />
                  </Stack.Item>
                  <Stack.Item style={{ width: '100%' }}>
                    <FormikCheckbox readOnly={false} isLight name="detectedPollution" label="Pollution détectée" />
                  </Stack.Item>
                  <Stack.Item style={{ width: '100%' }}>
                    <FormikCheckbox
                      readOnly={false}
                      isLight
                      name="pollutionObservedByAuthorizedAgent"
                      label="Pollution constatée par un agent habilité"
                    />
                  </Stack.Item>
                  <Stack.Item style={{ width: '100%' }}>
                    <FormikCheckbox readOnly={false} isLight name="diversionCarriedOut" label="Déroutement effectué" />
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

export default MissionActionItemAntiPollution
