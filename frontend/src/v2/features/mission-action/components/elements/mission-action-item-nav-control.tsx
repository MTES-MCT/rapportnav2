import { VesselTypeEnum } from '@common/types/mission-types'
import { FormikEffect } from '@mtes-mct/monitor-ui'
import { Field, FieldArray, FieldArrayRenderProps, FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { FormikDateRangePicker } from '../../../common/components/ui/formik-date-range-picker'
import { FormikSearchPort } from '../../../common/components/ui/formik-search-port'
import { FormikSelectLocationType } from '../../../common/components/ui/formik-select-location-type'
import { FormikSelectVesselSize } from '../../../common/components/ui/formik-select-vessel-size'
import { FormikTextInput } from '../../../common/components/ui/formik-text-input'
import { FormikTextAreaInput } from '../../../common/components/ui/formik-textarea-input'
import { SearchCity } from '../../../common/components/ui/search-city'
import { LocationType } from '../../../common/types/location-type'
import { MissionAction } from '../../../common/types/mission-action'
import MissionControlNavSummary from '../../../mission-control/components/ui/mission-control-nav-summary'
import MissionTargetControl from '../../../mission-target/components/elements/mission-target-control-nav'
import { useMissionActionNavControl } from '../../hooks/use-mission-action-nav-control'
import { ActionNavControlInput } from '../../types/action-type'
import { MissionActionFormikCoordinateInputDMD } from '../ui/mission-action-formik-coordonate-input-dmd'
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
          validateOnMount={true}
          validationSchema={validationSchema}
          enableReinitialize
        >
          {({ values, setFieldValue }) => (
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
                  <Field name="dates">
                    {(field: FieldProps<Date[]>) => (
                      <FormikDateRangePicker label="" name="dates" isLight={true} fieldFormik={field} />
                    )}
                  </Field>
                </Stack.Item>
                <Stack.Item style={{ width: '100%' }}>
                  <Stack direction={'row'} spacing={'1rem'}>
                    <Stack.Item style={{ width: '30%' }}>
                      <FormikSelectLocationType
                        name="locationType"
                        isLight={true}
                        label="Lieu de l'operation"
                        controlMethod={values.controlMethod}
                        onChangeEffect={() => setFieldValue('locationDescription', undefined)}
                      />
                    </Stack.Item>
                    <Stack.Item style={{ width: '70%' }}>
                      {values.locationType === LocationType.GPS && (
                        <Field name="geoCoords">
                          {(field: FieldProps<number[]>) => (
                            <MissionActionFormikCoordinateInputDMD name="geoCoords" fieldFormik={field} />
                          )}
                        </Field>
                      )}
                      {values.locationType === LocationType.PORT && (
                        <Field name="locationDescription">
                          {(field: FieldProps<string>) => (
                            <FormikSearchPort
                              name="locationDescription"
                              isLight={true}
                              label="Nom du port"
                              fieldFormik={field}
                            />
                          )}
                        </Field>
                      )}
                      {values.locationType === LocationType.COMMUNE && (
                        <Field name="locationDescription">
                          {(field: FieldProps<string>) => (
                            <SearchCity
                              name="locationDescription"
                              label="Nom de la commune"
                              isLight={true}
                              value={values.locationDescription}
                              onChange={value => setFieldValue('locationDescription', value)}
                              fieldFormik={field}
                            />
                          )}
                        </Field>
                      )}
                    </Stack.Item>
                  </Stack>
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
