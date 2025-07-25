import { FormikEffect, FormikTextarea } from '@mtes-mct/monitor-ui'
import { Field, FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { FormikDateRangePicker } from '../../../common/components/ui/formik-date-range-picker'
import { MissionAction } from '../../../common/types/mission-action'
import { useMissionActionIllegalImmigration } from '../../hooks/use-mission-action-illegal-immigration'
import { ActionIllegalImmigrationInput } from '../../types/action-type'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd'
import { MissionActionFormikNumberInput } from '../ui/mission-action-formik-number-input'

const MissionActionItemIllegalImmigration: FC<{
  action: MissionAction
  isMissionFinished?: boolean
  onChange: (newAction: MissionAction, debounceTime?: number) => Promise<unknown>
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
          enableReinitialize
        >
          {({ validateForm }) => (
            <>
              <FormikEffect
                onChange={async nextValue => {
                  // Only handle submission, let Formik handle validation display
                  await handleSubmit(nextValue as ActionIllegalImmigrationInput)
                  // Optionally trigger validation to ensure UI updates
                  await validateForm()
                }}
              />
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
          )}
        </Formik>
      )}
    </form>
  )
}

export default MissionActionItemIllegalImmigration
