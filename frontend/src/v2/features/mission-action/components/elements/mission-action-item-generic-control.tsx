import { ControlType } from '@common/types/control-types.ts'
import { FormikEffect, FormikTextarea } from '@mtes-mct/monitor-ui'
import { Field, FieldArray, FieldArrayRenderProps, FieldProps, Formik, FormikProps } from 'formik'
import React, { createElement, FunctionComponent } from 'react'
import { Stack } from 'rsuite'
import { ObjectShape } from 'yup'
import { FormikDateRangePicker } from '../../../common/components/ui/formik-date-range-picker.tsx'
import { useOnlineManager } from '../../../common/hooks/use-online-manager.tsx'
import { MissionAction } from '../../../common/types/mission-action.ts'
import MissionTargetList from '../../../mission-target/components/elements/mission-target-list.tsx'
import MissionTargetNew from '../../../mission-target/components/elements/mission-target-new.tsx'
import { useTarget } from '../../../mission-target/hooks/use-target.tsx'
import { useMissionActionGenericControl } from '../../hooks/use-mission-action-generic-control.tsx'
import { ActionControlInput, ActionEnvControlInput } from '../../types/action-type.ts'
import MissionActionDivingOperation from '../ui/mission-action-diving-operation.tsx'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd.tsx'
import MissionActionIncidentDonwload from '../ui/mission-action-incident-download.tsx'

export type MissionActionItemGenericControlProps = {
  action: MissionAction
  schema?: ObjectShape
  withGeoCoords?: boolean
  controlTypes?: ControlType[]
  isMissionFinished?: boolean
  isGeoCoordRequired?: boolean
  component?: FunctionComponent<{ formik: FormikProps<ActionControlInput> }>
  onChange: (newAction: MissionAction, debounceTime?: number) => Promise<unknown>
}

const MissionActionItemGenericControl: React.FC<MissionActionItemGenericControlProps> = ({
  action,
  schema,
  onChange,
  component,
  controlTypes,
  withGeoCoords,
  isMissionFinished,
  isGeoCoordRequired = true
}) => {
  const { isOnline } = useOnlineManager()
  const { defaultControlTypes } = useTarget()
  const { errors, initValue, handleSubmit, validationSchema } = useMissionActionGenericControl(
    action,
    onChange,
    schema,
    isMissionFinished,
    withGeoCoords,
    [
      'incidentDuringOperation',
      'isControlDuringSecurityDay',
      'isWithinDepartment',
      'hasDivingDuringOperation',
      'isSeizureSleepingFishingGear'
    ]
  )

  return (
    <form style={{ width: '100%' }}>
      {initValue && (
        <Formik
          initialValues={initValue}
          onSubmit={handleSubmit}
          validationSchema={validationSchema}
          validateOnChange={true}
          enableReinitialize={true}
          initialErrors={errors}
        >
          {formik => (
            <>
              <FormikEffect onChange={nextValues => handleSubmit(nextValues as ActionEnvControlInput)} />
              <Stack
                direction="column"
                spacing="1rem"
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
                  {withGeoCoords && (
                    <Field name="geoCoords">
                      {(field: FieldProps<number[]>) => (
                        <MissionActionFormikCoordinateInputDMD
                          isLight={true}
                          name="geoCoords"
                          fieldFormik={field}
                          isRequired={isGeoCoordRequired}
                        />
                      )}
                    </Field>
                  )}
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>{component && createElement(component, { formik })}</Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FieldArray name="targets">
                    {(fieldArray: FieldArrayRenderProps) => (
                      <MissionTargetNew
                        isDisabled={false} //TODO: how many target max we can have?
                        actionId={action.id}
                        fieldArray={fieldArray}
                        availableControlTypes={controlTypes ?? defaultControlTypes}
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
                        availableControlTypes={controlTypes ?? defaultControlTypes}
                      />
                    )}
                  </FieldArray>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FormikTextarea
                    isLight={true}
                    name="observations"
                    data-testid="observations"
                    label="Observation de l'unité sur le contrôle"
                  />
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

export default MissionActionItemGenericControl
