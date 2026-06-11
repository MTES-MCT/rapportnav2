import { ControlType } from '@common/types/control-types.ts'
import { FormikEffect } from '@mtes-mct/monitor-ui'
import { FieldArray, FieldArrayRenderProps, Formik, FormikProps } from 'formik'
import React, { createElement, FunctionComponent } from 'react'
import { Stack } from 'rsuite'
import { ObjectShape } from 'yup'
import { FormikTextAreaInput } from '../../../common/components/ui/formik-textarea-input.tsx'
import { useOnlineManager } from '../../../common/hooks/use-online-manager.tsx'
import { MissionAction } from '../../../common/types/mission-action.ts'
import MissionTargetNew from '../../../mission-target/components/elements/mission-target-new.tsx'
import TargetList from '../../../mission-target/components/elements/target-list.tsx'
import { useTarget } from '../../../mission-target/hooks/use-target.tsx'
import { useMissionActionGenericControl } from '../../hooks/use-mission-action-generic-control.tsx'
import { ActionControlInput } from '../../types/action-type.ts'
import MissionActionDivingOperation from '../ui/mission-action-diving-operation.tsx'
import MissionActionIncidentDonwload from '../ui/mission-action-incident-download.tsx'
import MissionBoundFormikDateRangePicker from '../../../common/components/elements/mission-bound-formik-date-range-picker.tsx'
import MissionActionLocationPicker from '../ui/mission-action-location-picker.tsx'
import { useFormValidationReporter } from '../../../common/hooks/use-form-validation-reporter'

export type MissionActionItemGenericControlProps = {
  action: MissionAction
  schema?: ObjectShape
  withGeoCoords?: boolean
  controlTypes?: ControlType[]
  component?: FunctionComponent<{ formik: FormikProps<ActionControlInput> }>
  onChange: (newAction: MissionAction, debounceTime?: number) => Promise<unknown>
}

const MissionActionItemGenericControl: React.FC<MissionActionItemGenericControlProps> = ({
  action,
  schema,
  onChange,
  component,
  controlTypes,
  withGeoCoords
}) => {
  const { isOnline } = useOnlineManager()
  const { defaultControlTypes } = useTarget()
  const { onFormError } = useFormValidationReporter()
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
              <FormikEffect onChange={async nextValues => handleSubmit(nextValues as ActionControlInput)} onError={onFormError} />
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
                    missionId={action.ownerId ?? action.missionId}
                    title={
                      isOnline
                        ? ''
                        : "Non disponible hors ligne, il est nécessaire d'être synchronisé avec les centres pour saisir/modifier cette donnée."
                    }
                  />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>{withGeoCoords && <MissionActionLocationPicker />}</Stack.Item>
                <Stack.Item style={{ width: '100%' }}>{component && createElement(component, { formik })}</Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FieldArray name="targets">
                    {(fieldArray: FieldArrayRenderProps) => (
                      <MissionTargetNew
                        isDisabled={false} //Question: how many target max we can have?
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
                      <TargetList
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
