import { ControlType } from '@common/types/control-types.ts'
import { FormikEffect } from '@mtes-mct/monitor-ui'
import { Field, FieldArray, FieldArrayRenderProps, FieldProps, Formik, FormikProps } from 'formik'
import React, { createElement, FunctionComponent } from 'react'
import { Stack } from 'rsuite'
import { ObjectShape } from 'yup'
import { FormikTextAreaInput } from '../../../common/components/ui/formik-textarea-input.tsx'
import { useOnlineManager } from '../../../common/hooks/use-online-manager.tsx'
import { MissionAction } from '../../../common/types/mission-action.ts'
import MissionTargetList from '../../../mission-target/components/elements/mission-target-list.tsx'
import MissionTargetNew from '../../../mission-target/components/elements/mission-target-new.tsx'
import { useTarget } from '../../../mission-target/hooks/use-target.tsx'
import { useMissionActionGenericControl } from '../../hooks/use-mission-action-generic-control.tsx'
import { ActionControlInput } from '../../types/action-type.ts'
import MissionActionDivingOperation from '../ui/mission-action-diving-operation.tsx'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd.tsx'
import MissionActionIncidentDonwload from '../ui/mission-action-incident-download.tsx'
import MissionBoundFormikDateRangePicker from '../../../common/components/elements/mission-bound-formik-date-range-picker.tsx'

export type MissionActionItemGenericControlProps = {
  action: MissionAction
  schema?: ObjectShape
  withGeoCoords?: boolean
  controlTypes?: ControlType[]
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
  isGeoCoordRequired = true
}) => {
  const { isOnline } = useOnlineManager()
  const { defaultControlTypes } = useTarget()
  const { initValue, handleSubmit, validationSchema } = useMissionActionGenericControl(
    action,
    onChange,
    schema,
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
          validateOnMount={true}
          enableReinitialize={true}
        >
          {formik => (
            <>
              <FormikEffect
                onChange={async nextValues => {
                  await handleSubmit(nextValues as ActionControlInput)
                  await formik.validateForm(nextValues)
                }}
              />
              <Stack
                direction="column"
                spacing="1rem"
                alignItems="flex-start"
                style={{ width: '100%' }}
                data-testid={'action-generic-control'}
              >
                <Stack.Item grow={1}>
                  <MissionBoundFormikDateRangePicker
                    isLight={true}
                    disabled={!isOnline}
                    missionId={action.missionId}
                    title={
                      isOnline
                        ? ''
                        : "Non disponible hors ligne, il est nécessaire d'être synchronisé avec les centres pour saisir/modifier cette donnée."
                    }
                  />
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
                  <FormikTextAreaInput
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
