import { VesselTypeEnum } from '@common/types/mission-types'
import { FormikEffect } from '@mtes-mct/monitor-ui'
import { Field, FieldArray, FieldArrayRenderProps, FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { FormikSelectVesselSize } from '../../../common/components/ui/formik-select-vessel-size'
import { FormikTextInput } from '../../../common/components/ui/formik-text-input'
import { FormikTextAreaInput } from '../../../common/components/ui/formik-textarea-input'
import { MissionAction } from '../../../common/types/mission-action'
import MissionControlNavSummary from '../../../mission-control/components/ui/mission-control-nav-summary'
import MissionTargetControl from '../../../mission-target/components/elements/mission-target-control-nav'
import { useMissionActionNavControl } from '../../hooks/use-mission-action-nav-control'
import { ActionNavControlInput } from '../../types/action-type'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd'
import MissionActionNavControlWarning from '../ui/mission-action-nav-control-warning'
import MissionBoundFormikDateRangePicker from '../../../common/components/elements/mission-bound-formik-date-range-picker.tsx'

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
          validateOnMount={true}
          validationSchema={validationSchema}
          enableReinitialize
        >
          {({ values }) => (
            <>
              <FormikEffect onChange={nextValue => handleSubmit(nextValue as ActionNavControlInput)} />
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
                  <MissionBoundFormikDateRangePicker isLight={true} missionId={action.ownerId ?? action.missionId} />
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
                      <FormikTextInput
                        name="vesselIdentifier"
                        label="Immatriculation"
                        data-testid={'vesselIdentifier'}
                      />
                    </Stack.Item>
                    <Stack.Item grow={2} basis={'50%'}>
                      <FormikTextInput
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
                  <FormikTextAreaInput
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
