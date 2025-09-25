import { FormikCheckbox, FormikEffect, FormikTextarea, THEME } from '@mtes-mct/monitor-ui'
import { Field, FieldArray, FieldArrayRenderProps, FieldProps, Formik } from 'formik'
import React, { JSX } from 'react'
import { Divider, Stack } from 'rsuite'
import { ObjectShape } from 'yup'
import { FormikDateRangePicker } from '../../../common/components/ui/formik-date-range-picker.tsx'
import { useOnlineManager } from '../../../common/hooks/use-online-manager.tsx'
import { MissionAction } from '../../../common/types/mission-action.ts'
import MissionTargetList from '../../../mission-target/components/elements/mission-target-list.tsx'
import MissionTargetNew from '../../../mission-target/components/elements/mission-target-new.tsx'
import { useTarget } from '../../../mission-target/hooks/use-target.tsx'
import { useMissionActionGenericControl } from '../../hooks/use-mission-action-generic-control.tsx'
import { ActionEnvControlInput } from '../../types/action-type.ts'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd.tsx'

export type MissionActionItemGenericControlProps = {
  action: MissionAction
  children?: JSX.Element
  schema?: ObjectShape
  withGeoCoords?: boolean
  isMissionFinished?: boolean
  onChange: (newAction: MissionAction, debounceTime?: number) => Promise<unknown>
}

const MissionActionItemGenericControl: React.FC<MissionActionItemGenericControlProps> = ({
  action,
  schema,
  onChange,
  children,
  withGeoCoords,
  isMissionFinished
}) => {
  const { isOnline } = useOnlineManager()
  const { controlTypes, computeControlTypeOnTarget } = useTarget()
  const { initValue, handleSubmit } = useMissionActionGenericControl(
    action,
    onChange,
    schema,
    isMissionFinished,
    withGeoCoords
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
                data-testid={'action-generic-control'}
              >
                <Stack.Item grow={1}>
                  <Field name="dates">
                    {(field: FieldProps<Date[]>) => (
                      <FormikDateRangePicker
                        label=""
                        name="dates"
                        isLight={true}
                        fieldFormik={field}
                        disabled={!isOnline}
                        title={
                          isOnline
                            ? ''
                            : "Non disponible hors ligne, il est nécessaire d'être synchronisé avec les centres pour saisir/modifier cette donnée."
                        }
                      />
                    )}
                  </Field>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  {withGeoCoords && <MissionActionFormikCoordinateInputDMD name={'geoCoords'} isLight={true} />}
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>{children}</Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FieldArray name="targets">
                    {(fieldArray: FieldArrayRenderProps) => (
                      <MissionTargetNew
                        isDisabled={false} //TODO: how many target max we can have?
                        actionId={action.id}
                        fieldArray={fieldArray}
                        availableControlTypes={computeControlTypeOnTarget(controlTypes, values.targets)}
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
                        actionNumberOfControls={0}
                        controlsToComplete={[]}
                        availableControlTypes={computeControlTypeOnTarget(controlTypes, values.targets)}
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
                <Stack.Item style={{ width: '100%' }}>
                  <Divider style={{ backgroundColor: THEME.color.charcoal, marginBottom: 4 }} />
                  <FormikCheckbox isLight name="hasDivingDuringOperation" label="Plongée au cours de l'opération" />
                </Stack.Item>
              </Stack>
            </>
          )}
        </Formik>
      )}
    </form>
  )
}

export default MissionActionItemGenericControl
