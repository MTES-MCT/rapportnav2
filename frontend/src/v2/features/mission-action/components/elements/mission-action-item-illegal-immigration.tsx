import { FormikEffect, FormikTextarea } from '@mtes-mct/monitor-ui'
import { Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { MissionActionOutput } from '../../../common/types/mission-action-output'
import { useMissionActionIllegalImmigration } from '../../hooks/use-mission-action-illegal-immigration'
import { ActionIllegalImmigrationInput } from '../../types/action-type'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd'
import { MissionActionFormikDateRangePicker } from '../ui/mission-action-formik-date-range-picker'
import { MissionActionFormikNumberInput } from '../ui/mission-action-formik-number-input'

const MissionActionItemIllegalImmigration: FC<{
  action: MissionActionOutput
  isMissionFinished?: boolean
  onChange: (newAction: MissionActionOutput, debounceTime?: number) => Promise<unknown>
}> = ({ action, onChange }) => {
  const { initValue, validationSchema, handleSubmit } = useMissionActionIllegalImmigration(action, onChange)

  return (
    <form style={{ width: '100%' }} data-testid={'action-nautical-event-form'}>
      {initValue && (
        <Formik
          validateOnChange={true}
          onSubmit={handleSubmit}
          initialValues={initValue}
          validationSchema={validationSchema}
        >
          <>
            <FormikEffect onChange={nextValue => handleSubmit(nextValue as ActionIllegalImmigrationInput)} />
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
                    />
                  </Stack.Item>
                  <Stack.Item style={{ width: '100%' }}>
                    <Stack direction={'row'} spacing={'1rem'}>
                      <Stack.Item style={{ flex: 1 }}>
                        <MissionActionFormikNumberInput
                          name="nbOfInterceptedMigrants"
                          role="nbOfInterceptedMigrants"
                          label="Nb de migrants interceptés"
                        />
                      </Stack.Item>
                      <Stack.Item style={{ flex: 1 }}>
                        <MissionActionFormikNumberInput
                          name="nbOfSuspectedSmugglers"
                          role="nbOfSuspectedSmugglers"
                          label="Nb de passeurs présumés"
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
