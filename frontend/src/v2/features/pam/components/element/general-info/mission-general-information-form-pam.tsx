import { FC } from 'react'
import { FormikEffect, FormikNumberInput, FormikTextarea, Label, THEME } from '@mtes-mct/monitor-ui'
import { Field, FieldArray, FieldArrayRenderProps, FieldProps, Formik } from 'formik'
import { Stack } from 'rsuite'
import { MissionGeneralInfo2 } from '../../../../common/types/mission-types.ts'
import { FormikDateRangePicker } from '../../../../common/components/ui/formik-date-range-picker.tsx'
import {
  MissionPAMGeneralInfoInitialInput,
  usePamMissionGeneralInfoForm
} from '../../../hooks/use-pam-mission-general-information-form.tsx'
import MissionGeneralInformationCrewPam from './crew/mission-general-information-crew-pam.tsx'
import MissionService from './mission-service.tsx'
import MissionGeneralInformationPassengerPam from './passengers/mission-general-information-passenger-pam.tsx'
import { FormikNumberInputDelay } from '../../../../common/components/ui/formik-number-input-delay.tsx'
import { FormikTextareaInputDelay } from '../../../../common/components/ui/formik-text-area-delay.tsx'

const MissionGeneralInformationFormPam: FC<{
  generalInfo2: MissionGeneralInfo2
  onChange: (newGeneralInfo: MissionGeneralInfo2) => Promise<unknown>
}> = ({ generalInfo2, onChange }) => {
  const { handleSubmit, initValue, validationSchema, errors } = usePamMissionGeneralInfoForm(onChange, generalInfo2)

  return (
    <Stack.Item style={{ backgroundColor: THEME.color.white, width: '100%' }}>
      {initValue && (
        <Formik
          initialValues={initValue}
          onSubmit={handleSubmit}
          enableReinitialize={true}
          validateOnChange={true}
          validationSchema={validationSchema}
          initialErrors={errors}
        >
          {({ validateForm }) => (
            <>
              <FormikEffect
                onChange={async nextValue => {
                  // Only handle submission, let Formik handle validation display
                  await handleSubmit(nextValue as MissionPAMGeneralInfoInitialInput)
                  // Optionally trigger validation to ensure UI updates
                  await validateForm()
                }}
              />
              <Stack
                direction="column"
                style={{ width: '100%', padding: '0 2px' }}
                alignItems={'flex-start'}
                spacing={'2rem'}
              >
                <Stack.Item style={{ width: '100%' }}>
                  <Stack direction="row" alignItems={'flex-start'} justifyContent={'flex-end'}>
                    <Stack.Item style={{ width: '80%' }}>
                      <Field name="dates">
                        {(field: FieldProps<Date[]>) => (
                          <FormikDateRangePicker
                            label=""
                            name="dates"
                            isLight={false}
                            fieldFormik={field}
                            validateOnSubmit={false}
                            isCompact={true}
                          />
                        )}
                      </Field>
                    </Stack.Item>
                    <Stack.Item style={{ width: '20%' }}>
                      <MissionService services={generalInfo2.services} />
                    </Stack.Item>
                  </Stack>
                </Stack.Item>

                <Stack.Item style={{ width: '100%' }}>
                  <FieldArray name="crew">
                    {(fieldArray: FieldArrayRenderProps) => (
                      <MissionGeneralInformationCrewPam
                        name="crew"
                        missionId={generalInfo2.missionId}
                        currentCrewList={generalInfo2.crew ?? []}
                        fieldArray={fieldArray}
                      />
                    )}
                  </FieldArray>
                </Stack.Item>

                <Stack.Item style={{ width: '100%' }}>
                  <FieldArray name="passengers">
                    {(fieldArray: FieldArrayRenderProps) => (
                      <MissionGeneralInformationPassengerPam
                        name="passengers"
                        missionId={generalInfo2.missionId}
                        currentPassengerList={generalInfo2.passengers ?? []}
                        fieldArray={fieldArray}
                      />
                    )}
                  </FieldArray>
                </Stack.Item>

                <Stack.Item style={{ width: '100%' }}>
                  <Label>Distance et consommation</Label>
                  <Stack
                    direction="row"
                    alignItems="flex-start"
                    spacing="1rem"
                    style={{ width: '100%', backgroundColor: THEME.color.gainsboro, padding: '0.5rem' }}
                  >
                    <Stack.Item style={{ flex: 1 }}>
                      <Field name="distanceInNauticalMiles">
                        {(field: FieldProps<number>) => (
                          <FormikNumberInputDelay
                            fieldFormik={field}
                            label="Distance parcourue en milles"
                            name="distanceInNauticalMiles"
                            role="distanceInNauticalMiles"
                            isRequired={true}
                            placeholder="0"
                            isLight={true}
                            isErrorMessageHidden={true}
                          />
                        )}
                      </Field>
                    </Stack.Item>
                    <Stack.Item style={{ flex: 1 }}>
                      <Field name="consumedGOInLiters">
                        {(field: FieldProps<number>) => (
                          <FormikNumberInputDelay
                            fieldFormik={field}
                            label="GO marine consommé en litres"
                            name="consumedGOInLiters"
                            role="consumedGOInLiters"
                            isRequired={true}
                            placeholder="0"
                            isLight={true}
                            isErrorMessageHidden={true}
                          />
                        )}
                      </Field>
                    </Stack.Item>
                    <Stack.Item style={{ flex: 1 }}>
                      <Field name="consumedFuelInLiters">
                        {(field: FieldProps<number>) => (
                          <FormikNumberInputDelay
                            fieldFormik={field}
                            label="Essence consommée en litres"
                            name="consumedFuelInLiters"
                            role="consumedFuelInLiters"
                            isRequired={true}
                            placeholder="0"
                            isLight={true}
                            isErrorMessageHidden={true}
                          />
                        )}
                      </Field>
                    </Stack.Item>
                  </Stack>
                </Stack.Item>

                <Stack.Item style={{ width: '100%' }}>
                  <Label>Dépenses réalisées au cours de la mission</Label>
                  <Stack
                    direction="row"
                    alignItems="flex-start"
                    spacing="1rem"
                    style={{ width: '100%', backgroundColor: THEME.color.gainsboro, padding: '0.5rem' }}
                  >
                    <Stack.Item style={{ flex: 1 }}>
                      <Field name="operatingCostsInEuro">
                        {(field: FieldProps<number>) => (
                          <FormikNumberInputDelay
                            fieldFormik={field}
                            label="Fonctionnement courant (€)"
                            name="operatingCostsInEuro"
                            role="operatingCostsInEuro"
                            placeholder="0"
                            isLight={true}
                            isErrorMessageHidden={true}
                          />
                        )}
                      </Field>
                    </Stack.Item>
                    <Stack.Item style={{ flex: 1 }}>
                      <Field name="fuelCostsInEuro">
                        {(field: FieldProps<number>) => (
                          <FormikNumberInputDelay
                            fieldFormik={field}
                            label="Carburant GO/essence (€)"
                            name="fuelCostsInEuro"
                            role="fuelCostsInEuro"
                            placeholder="0"
                            isLight={true}
                            isErrorMessageHidden={true}
                          />
                        )}
                      </Field>
                    </Stack.Item>
                  </Stack>
                </Stack.Item>

                <Stack.Item style={{ width: '100%' }}>
                  <Field name="nbrOfRecognizedVessel">
                    {(field: FieldProps<number>) => (
                      <FormikNumberInputDelay
                        fieldFormik={field}
                        isLight={false}
                        isRequired={true}
                        name="nbrOfRecognizedVessel"
                        data-testid="mission-information-general-recognized-vessel"
                        label="Nombre total de navires reconnus dans les approches maritimes ZEE"
                        isErrorMessageHidden={true}
                      />
                    )}
                  </Field>
                </Stack.Item>

                <Stack.Item style={{ width: '100%' }}>
                  <Field name="observations">
                    {(field: FieldProps<string>) => (
                      <FormikTextareaInputDelay
                        fieldFormik={field}
                        isLight={false}
                        name="observations"
                        data-testid="mission-general-observation"
                        label="Observation générale à l'échelle de la mission (remarques, résumé)"
                        isErrorMessageHidden={true}
                      />
                    )}
                  </Field>
                </Stack.Item>
              </Stack>
            </>
          )}
        </Formik>
      )}
    </Stack.Item>
  )
}

export default MissionGeneralInformationFormPam
