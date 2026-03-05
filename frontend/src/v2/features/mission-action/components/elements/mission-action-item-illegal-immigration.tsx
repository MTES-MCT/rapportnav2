import { FormikEffect, FormikTextarea } from '@mtes-mct/monitor-ui'
import { Field, FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { MissionAction } from '../../../common/types/mission-action'
import { useMissionActionIllegalImmigration } from '../../hooks/use-mission-action-illegal-immigration'
import { ActionIllegalImmigrationInput } from '../../types/action-type'
import MissionActionDivingOperation from '../ui/mission-action-diving-operation'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd'
import { MissionActionFormikNumberInput } from '../ui/mission-action-formik-number-input'
import MissionActionIncidentDonwload from '../ui/mission-action-incident-download'
import MissionBoundFormikDateRangePicker from '../../../common/components/elements/mission-bound-formik-date-range-picker.tsx'

const MissionActionItemIllegalImmigration: FC<{
  action: MissionAction
  isMissionFinished?: boolean
  onChange: (newAction: MissionAction, debounceTime?: number) => Promise<unknown>
}> = ({ action, onChange }) => {
  const { initValue, errors, validationSchema, handleSubmit } = useMissionActionIllegalImmigration(action, onChange)

  return (
    <form style={{ width: '100%' }} data-testid={'action-illegal-immigration-form'}>
      {initValue && (
        <Formik
          validateOnChange={true}
          onSubmit={handleSubmit}
          initialValues={initValue}
          initialErrors={errors}
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
                  await validateForm(nextValue)
                }}
              />
              <Stack direction="column" spacing="2rem" alignItems="flex-start" style={{ width: '100%' }}>
                <Stack.Item style={{ width: '100%' }}>
                  <Stack direction="row" spacing="0.5rem" style={{ width: '100%' }}>
                    <Stack.Item grow={1}>
                      <MissionBoundFormikDateRangePicker isLight={true} missionId={action.missionId} />
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <Field name="geoCoords">
                    {(field: FieldProps<number[]>) => (
                      <MissionActionFormikCoordinateInputDMD name="geoCoords" fieldFormik={field} />
                    )}
                  </Field>
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
                  <MissionActionIncidentDonwload />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <MissionActionDivingOperation />
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
