import { VesselTypeEnum } from '@common/types/mission-types'
import { FormikEffect, FormikTextarea } from '@mtes-mct/monitor-ui'
import { Field, FieldArray, FieldArrayRenderProps, FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { FormikDateRangePicker } from '../../../common/components/ui/formik-date-range-picker'
import { FormikSelectVesselSize } from '../../../common/components/ui/formik-select-vessel-size'
import { MissionAction } from '../../../common/types/mission-action'
import MissionControlNavSummary from '../../../mission-control/components/ui/mission-control-nav-summary'
import MissionTargetControl from '../../../mission-target/components/elements/mission-target-control-nav'
import { useMissionActionNavControl } from '../../hooks/use-mission-action-nav-control'
import { ActionNavControlInput } from '../../types/action-type'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd'
import { MissionActionFormikTextInput } from '../ui/mission-action-formik-text-input'
import MissionActionNavControlWarning from '../ui/mission-action-nav-control-warning'

const MissionActionItemNavControl: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction) => Promise<unknown>
}> = ({ action, onChange }) => {
  const { initValue, handleSubmit, validationSchema } = useMissionActionNavControl(action, onChange)

  return (
    <div style={{ width: '100%' }}>
      {initValue && (
        <Formik
          initialValues={initValue}
          onSubmit={handleSubmit}
          validateOnChange={true}
          validationSchema={validationSchema}
          enableReinitialize
        >
          {({ validateForm, values }) => (
            <>
              <FormikEffect
                onChange={async nextValue => {
                  // Only handle submission, let Formik handle validation display
                  await handleSubmit(nextValue as ActionNavControlInput)
                  // Optionally trigger validation to ensure UI updates
                  await validateForm()
                }}
              />
              <Stack
                direction="column"
                spacing="2rem"
                alignItems="flex-start"
                style={{ width: '100%' }}
                data-testid={'action-control-nav'}
              >
                <Stack.Item>
                  <MissionActionNavControlWarning />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <MissionControlNavSummary vesselType={values?.vesselType} controlMethod={values?.controlMethod} />
                </Stack.Item>
                <Stack.Item grow={1}>
                  <Field name="dates">
                    {(field: FieldProps<Date[]>) => (
                      <FormikDateRangePicker label="" name="dates" isLight={true} fieldFormik={field} />
                    )}
                  </Field>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <Field name="geoCoords">
                    {(field: FieldProps<number[]>) => (
                      <MissionActionFormikCoordinateInputDMD name="geoCoords" fieldFormik={field} />
                    )}
                  </Field>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <Stack spacing="0.5rem" style={{ width: '100%' }}>
                    <Stack.Item grow={1} basis={'25%'}>
                      <FormikSelectVesselSize name="vesselSize" label="Taille du navire" data-testid={'vesselSize'} />
                    </Stack.Item>
                    <Stack.Item grow={1} basis={'25%'}>
                      <MissionActionFormikTextInput
                        name="vesselIdentifier"
                        label="Immatriculation"
                        data-testid={'vesselIdentifier'}
                      />
                    </Stack.Item>
                    <Stack.Item grow={2} basis={'50%'}>
                      <MissionActionFormikTextInput
                        label="Identité de la personne contrôlée"
                        name="identityControlledPerson"
                        data-testid={'identityControlledPerson'}
                      />
                    </Stack.Item>
                  </Stack>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FieldArray name="targets">
                    {(fieldArray: FieldArrayRenderProps) => (
                      <MissionTargetControl
                        name="targets"
                        fieldArray={fieldArray}
                        controlsToComplete={action.controlsToComplete}
                        label={`Contrôle(s) effectué(s) par l’unité sur le navire`}
                        hideGensDeMer={values.vesselType === VesselTypeEnum.SAILING_LEISURE}
                      />
                    )}
                  </FieldArray>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FormikTextarea
                    isLight={true}
                    name="observations"
                    data-testid="observations"
                    label="Observations générales sur le contrôle"
                  />
                </Stack.Item>
              </Stack>
            </>
          )}
        </Formik>
      )}
    </div>
  )
}

export default MissionActionItemNavControl
