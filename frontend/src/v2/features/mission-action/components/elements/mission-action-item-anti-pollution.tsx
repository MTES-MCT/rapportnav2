import { FormikCheckbox, FormikEffect, FormikTextarea, Icon, THEME } from '@mtes-mct/monitor-ui'
import { Field, FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { FormikDateRangePicker } from '../../../common/components/ui/formik-date-range-picker'
import { StyledFormikToggle } from '../../../common/components/ui/formik-styled-toogle'
import { MissionAction } from '../../../common/types/mission-action'
import { useMissionActionAntiPollution } from '../../hooks/use-mission-action-anti-pollution'
import { ActionAntiPollutionInput } from '../../types/action-type'
import MissionActionAntiPollutionWarning from '../ui/mission-action-anti-pollution-warning'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd'

const MissionActionItemAntiPollution: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  const { initValue, handleSubmit, validationSchema } = useMissionActionAntiPollution(action, onChange)
  return (
    <form style={{ width: '100%' }} data-testid={'action-nautical-event-form'}>
      {initValue && (
        <Formik
          initialValues={initValue}
          onSubmit={handleSubmit}
          validateOnChange={true}
          validationSchema={validationSchema}
          enableReinitialize
        >
          {({ validateForm }) => (
            <>
              <FormikEffect
                onChange={async nextValue => {
                  // Only handle submission, let Formik handle validation display
                  await handleSubmit(nextValue as ActionAntiPollutionInput)
                  // Optionally trigger validation to ensure UI updates
                  await validateForm()
                }}
              />
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
                      <FormikCheckbox
                        readOnly={false}
                        isLight
                        name="diversionCarriedOut"
                        label="Déroutement effectué"
                      />
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

export default MissionActionItemAntiPollution
