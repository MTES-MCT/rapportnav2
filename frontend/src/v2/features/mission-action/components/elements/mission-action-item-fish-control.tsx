import { MissionActionType } from '@common/types/fish-mission-types.ts'
import { FormikEffect, FormikTextarea, Label, TextInput, THEME } from '@mtes-mct/monitor-ui'
import { Field, FieldArray, FieldArrayRenderProps, FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Divider, Stack } from 'rsuite'
import { FormikDateRangePicker } from '../../../common/components/ui/formik-date-range-picker'
import MissionIncompleteControlTag from '../../../common/components/ui/mission-incomplete-control-tag'
import VesselName from '../../../common/components/ui/vessel-name'
import { MissionAction } from '../../../common/types/mission-action'
import MissionControlFishAdministrativeSection from '../../../mission-control/components/ui/mission-control-fish-administrative-section'
import FishControlEnginesSection from '../../../mission-control/components/ui/mission-control-fish-engines-section'
import FishControlFleetSegmentSection from '../../../mission-control/components/ui/mission-control-fish-fleet-segment-section'
import FishControlOtherInfractionsSection from '../../../mission-control/components/ui/mission-control-fish-other-infractions-section'
import FishControlOtherObservationsSection from '../../../mission-control/components/ui/mission-control-fish-other-observation-section'
import FishControlSeizureSection from '../../../mission-control/components/ui/mission-control-fish-seizure-section'
import FishControlSpeciesSection from '../../../mission-control/components/ui/mission-control-fish-species-section'
import MissionTargetControlNav from '../../../mission-target/components/elements/mission-target-control-nav.tsx'
import { useMissionActionFishControl } from '../../hooks/use-mission-action-fish-control'
import { ActionFishControlInput } from '../../types/action-type'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd'

const MissionActionItemFishControl: FC<{
  action: MissionAction
  onChange: (newAction: MissionAction, debounceTime?: number) => Promise<unknown>
  isMissionFinished?: boolean
}> = ({ action, onChange, isMissionFinished }) => {
  const { initValue, handleSubmit } = useMissionActionFishControl(action, onChange, isMissionFinished)
  return (
    <div style={{ width: '100%' }}>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} validateOnChange={true} enableReinitialize>
          {({ values }) => (
            <>
              <FormikEffect onChange={nextValues => handleSubmit(nextValues as ActionFishControlInput)} />
              <Stack
                direction="column"
                spacing="2rem"
                alignItems="flex-start"
                style={{ width: '100%' }}
                data-testid={'action-control-nav'}
              >
                <Stack.Item style={{ width: '100%' }}>
                  <VesselName
                    name={values.vesselName}
                    flagState={values.flagState}
                    externalReferenceNumber={values.externalReferenceNumber}
                  />
                </Stack.Item>
                <Stack.Item grow={1}>
                  <Field name="dates">
                    {(field: FieldProps<Date[]>) => (
                      <FormikDateRangePicker label="" name="dates" isLight={true} fieldFormik={field} />
                    )}
                  </Field>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  {initValue?.fishActionType === MissionActionType.LAND_CONTROL ? (
                    <TextInput
                      label={"Lieu de l'opération"}
                      readOnly={true}
                      value={`${initValue?.portName} ${initValue.portLocode ? `(${initValue.portLocode})` : ''}`}
                      isLight={true}
                      data-testid={'portName'}
                      name="portName"
                    />
                  ) : (
                    <MissionActionFormikCoordinateInputDMD
                      name={'geoCoords'}
                      readOnly={true}
                      isLight={true}
                      data-testid="mission-coordinate-dmd-input"
                    />
                  )}
                </Stack.Item>

                <Stack.Item style={{ width: '100%' }}>
                  <Divider style={{ backgroundColor: THEME.color.charcoal }} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <MissionControlFishAdministrativeSection action={values} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FishControlEnginesSection action={values} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FishControlSpeciesSection action={values} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FishControlSeizureSection action={values} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FishControlOtherInfractionsSection action={values} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FishControlOtherObservationsSection action={values} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <Divider style={{ backgroundColor: THEME.color.charcoal }} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FishControlFleetSegmentSection action={values} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <Stack direction="column" alignItems="flex-start">
                    <Stack.Item>
                      <Label>Saisi par</Label>
                    </Stack.Item>
                    <Stack.Item>{values.userTrigram ?? '--'}</Stack.Item>
                  </Stack>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <Divider style={{ backgroundColor: THEME.color.charcoal }} />
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  {(action?.controlsToComplete?.length ?? 0) > 0 && (
                    <Stack.Item alignSelf="flex-end">
                      <MissionIncompleteControlTag
                        isLight={true}
                        nbrIncompleteControl={action?.controlsToComplete?.length}
                      />
                    </Stack.Item>
                  )}
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <FieldArray name="targets">
                    {(fieldArray: FieldArrayRenderProps) => (
                      <MissionTargetControlNav
                        name="targets"
                        fieldArray={fieldArray}
                        controlsToComplete={action.controlsToComplete}
                        label={`Autre(s) contrôle(s) effectué(s) par l’unité sur le navire`}
                      />
                    )}
                  </FieldArray>
                </Stack.Item>

                <Stack.Item style={{ width: '100%' }}>
                  <FormikTextarea
                    isLight={true}
                    name="observationsByUnit"
                    data-testid="observations-by-unit"
                    label="Observation de l'unité sur le contrôle"
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

export default MissionActionItemFishControl
