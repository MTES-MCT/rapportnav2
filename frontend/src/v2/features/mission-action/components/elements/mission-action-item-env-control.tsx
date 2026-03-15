import Text from '@common/components/ui/text'
import { FormikEffect, FormikTextarea, THEME } from '@mtes-mct/monitor-ui'
import { Field, FieldArray, FieldArrayRenderProps, FieldProps, Formik } from 'formik'
import React from 'react'
import { Divider, Stack } from 'rsuite'
import MissionIncompleteControlTag from '../../../common/components/ui/mission-incomplete-control-tag'
import { MissionAction } from '../../../common/types/mission-action'
import MissionTargetControlEnv from '../../../mission-target/components/elements/mission-target-control-env.tsx'
import MissionTargetList from '../../../mission-target/components/elements/mission-target-list.tsx'
import MissionTargetNew from '../../../mission-target/components/elements/mission-target-new.tsx'
import { useTarget } from '../../../mission-target/hooks/use-target.tsx'
import { useMissionActionEnvControl } from '../../hooks/use-mission-action-env-control'
import { ActionEnvControlInput } from '../../types/action-type'
import MissionActionEnvControlSummary from '../ui/mission-action-env-control-summary'
import MissionActionEnvThemes from '../ui/mission-action-env-themes.tsx'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd'
import MissionBoundFormikDateRangePicker from '../../../common/components/elements/mission-bound-formik-date-range-picker.tsx'

export type MissionActionItemEnvControlProps = {
  action: MissionAction
  onChange: (newAction: MissionAction, debounceTime?: number) => Promise<unknown>
}

const MissionActionItemEnvControl: React.FC<MissionActionItemEnvControlProps> = ({ action, onChange }) => {
  const { getAvailableEnvControlTypes } = useTarget()
  const { initValue, handleSubmit, validationSchema } = useMissionActionEnvControl(action, onChange)

  return (
    <form style={{ width: '100%' }}>
      {initValue && (
        <Formik
          initialValues={initValue}
          onSubmit={handleSubmit}
          validateOnChange={true}
          validationSchema={validationSchema}
          enableReinitialize={true}
        >
          {({ values, validateForm }) => (
            <>
              <FormikEffect
                onChange={async nextValues => {
                  await handleSubmit(nextValues as ActionEnvControlInput)
                  await validateForm(nextValues)
                }}
              />
              <Stack
                direction="column"
                spacing="2rem"
                alignItems="flex-start"
                style={{ width: '100%' }}
                data-testid={'action-control-nav'}
              >
                <Stack.Item style={{ width: '100%' }}>
                  <MissionActionEnvThemes themes={values?.themes} />
                </Stack.Item>
                <Stack.Item grow={1}>
                  <MissionBoundFormikDateRangePicker isLight={true} missionId={action.ownerId ?? action.missionId} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <Field name="geoCoords">
                    {(field: FieldProps<number[]>) => (
                      <MissionActionFormikCoordinateInputDMD
                        readOnly={true}
                        isLight={true}
                        name="geoCoords"
                        fieldFormik={field}
                      />
                    )}
                  </Field>
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
                              <MissionTargetControlEnv
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
                        availableControlTypes={getAvailableEnvControlTypes(
                          values.targets,
                          values.availableControlTypesForInfraction
                        )}
                      />
                    )}
                  </FieldArray>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FieldArray name="targets">
                    {(fieldArray: FieldArrayRenderProps) => (
                      <MissionTargetList
                        name="targets"
                        fieldArray={fieldArray}
                        vehicleType={values.vehicleType}
                        actionTargetType={values.actionTargetType}
                        controlsToComplete={values.controlsToComplete}
                        actionNumberOfControls={values.actionNumberOfControls}
                        availableControlTypes={getAvailableEnvControlTypes(
                          values.targets,
                          values.availableControlTypesForInfraction
                        )}
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

export default MissionActionItemEnvControl
