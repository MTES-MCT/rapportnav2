import Text from '@common/components/ui/text'
import { FormikEffect, FormikTextarea, THEME } from '@mtes-mct/monitor-ui'
import { Field, FieldArray, FieldArrayRenderProps, FieldProps, Formik } from 'formik'
import React from 'react'
import { Divider, Stack } from 'rsuite'
import { FormikDateRangePicker } from '../../../common/components/ui/formik-date-range-picker'
import MissionIncompleteControlTag from '../../../common/components/ui/mission-incomplete-control-tag'
import { MissionAction } from '../../../common/types/mission-action'
import MissionTargetEnv from '../../../mission-target/components/elements/mission-target-env'
import MissionTargetDefault from '../../../mission-target/components/elements/mission-target-env-default'
import MissionTargetNew from '../../../mission-target/components/elements/mission-target-env-new'
import { useMissionActionEnvControl } from '../../hooks/use-mission-action-env-control'
import { ActionEnvControlInput } from '../../types/action-type'
import MissionActionEnvControlPlan from '../ui/mission-action-env-control-plan'
import MissionActionEnvControlSummary from '../ui/mission-action-env-control-summary'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd'

type MissionActionItemEnvControl2Props = {
  action: MissionAction
  isMissionFinished?: boolean
  onChange: (newAction: MissionAction, debounceTime?: number) => Promise<unknown>
}

const MissionActionItemEnvControl2: React.FC<MissionActionItemEnvControl2Props> = ({
  action,
  onChange,
  isMissionFinished
}) => {
  const { initValue, handleSubmit, getAvailableControlTypes2 } = useMissionActionEnvControl(
    action,
    onChange,
    isMissionFinished
  )

  return (
    <form style={{ width: '100%' }}>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} validateOnChange={true} enableReinitialize={true}>
          {({ values }) => (
            <>
              <FormikEffect onChange={nextValues => handleSubmit(nextValues as ActionEnvControlInput)} />
              <Stack
                direction="column"
                spacing="2rem"
                alignItems="flex-start"
                style={{ width: '100%' }}
                data-testid={'action-control-nav'}
              >
                <Stack.Item style={{ width: '100%' }}>
                  <MissionActionEnvControlPlan controlPlans={values?.formattedControlPlans} />
                </Stack.Item>
                <Stack.Item grow={1}>
                  <Field name="dates">
                    {(field: FieldProps<Date[]>) => (
                      <FormikDateRangePicker label="" name="dates" isLight={true} fieldFormik={field} />
                    )}
                  </Field>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <MissionActionFormikCoordinateInputDMD name={'geoCoords'} readOnly={true} isLight={true} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <Divider style={{ backgroundColor: THEME.color.charcoal }} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <Stack direction="row" alignItems="flex-start" spacing={'2rem'} style={{ width: '100%' }}>
                    <Stack.Item style={{ width: '33%' }}>
                      <MissionActionEnvControlSummary data={values} />
                    </Stack.Item>
                    <Stack.Item style={{ width: '67%' }}>
                      <Stack direction="column" alignItems="flex-start" spacing={'1.5rem'} style={{ width: '100%' }}>
                        {(values?.controlsToComplete?.length ?? 0) > 0 && (
                          <Stack.Item alignSelf="flex-end">
                            <MissionIncompleteControlTag
                              isLight={true}
                              nbrIncompleteControl={values?.controlsToComplete?.length}
                            />
                          </Stack.Item>
                        )}

                        <Stack.Item>
                          <Text as="h3" weight="bold" color={THEME.color.gunMetal}>
                            dont...
                          </Text>
                        </Stack.Item>
                        <Stack.Item style={{ width: '100%' }}>
                          <FieldArray name="targets">
                            {(fieldArray: FieldArrayRenderProps) => (
                              <MissionTargetDefault
                                name="targets"
                                fieldArray={fieldArray}
                                controlsToComplete={values.controlsToComplete}
                                actionNumberOfControls={values.actionNumberOfControls}
                              />
                            )}
                          </FieldArray>
                        </Stack.Item>
                      </Stack>
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FieldArray name="targets">
                    {(fieldArray: FieldArrayRenderProps) => (
                      <MissionTargetNew
                        isDisabled={false} //TODO: how many target max we can have?
                        actionId={action.id}
                        fieldArray={fieldArray}
                        vehicleType={values.vehicleType}
                        actionTargetType={values.actionTargetType}
                        controlsToComplete={values.controlsToComplete}
                        availableControlTypes={getAvailableControlTypes2(values, values.actionNumberOfControls)}
                      />
                    )}
                  </FieldArray>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FieldArray name="targets">
                    {(fieldArray: FieldArrayRenderProps) => (
                      <MissionTargetEnv
                        name="targets"
                        fieldArray={fieldArray}
                        vehicleType={values.vehicleType}
                        actionTargetType={values.actionTargetType}
                        controlsToComplete={values.controlsToComplete}
                        actionNumberOfControls={values.actionNumberOfControls}
                        availableControlTypes={getAvailableControlTypes2(values, values.actionNumberOfControls)}
                      />
                    )}
                  </FieldArray>
                </Stack.Item>

                <Stack.Item style={{ width: '100%' }}>
                  <FormikTextarea
                    isLight={true}
                    name="observationsByUnit"
                    data-testid="observationsByUnit"
                    label="Observation de l'unité sur le contrôle"
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

export default MissionActionItemEnvControl2
