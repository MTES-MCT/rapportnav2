import { FC } from 'react'
import { FormikEffect, FormikNumberInput, FormikTextarea, Label, THEME } from '@mtes-mct/monitor-ui'
import { Field, FieldArray, FieldArrayRenderProps, FieldProps, Formik } from 'formik'
import { Stack } from 'rsuite'
import { MissionGeneralInfo2 } from '../../../../common/types/mission-types.ts'
import { MissionGeneralInfoInput } from '../../../../ulam/hooks/use-ulam-mission-general-information-form.tsx'
import { FormikDateRangePicker } from '../../../../common/components/ui/formik-date-range-picker.tsx'
import { usePamMissionGeneralInfoForm } from '../../../hooks/use-pam-mission-general-information-form.tsx'
import MissionGeneralInformationCrewPam from './mission-general-information-crew-pam.tsx'
import MissionService from './mission-service.tsx'

const MissionGeneralInformationFormPam: FC<{
  generalInfo2: MissionGeneralInfo2
  onChange: (newGeneralInfo: MissionGeneralInfo2) => Promise<unknown>
}> = ({ generalInfo2, onChange }) => {
  const { handleSubmit, initValue } = usePamMissionGeneralInfoForm(onChange, generalInfo2)

  return (
    <Stack.Item style={{ backgroundColor: THEME.color.white, width: '100%' }}>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} enableReinitialize={true}>
          <>
            <FormikEffect onChange={newValues => handleSubmit(newValues as MissionGeneralInfoInput)} />
            <Stack direction="column" style={{ width: '100%' }} alignItems={'flex-start'} spacing={'2rem'}>
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
                <Label>Distance et consommation</Label>
                <Stack
                  direction="row"
                  alignItems="flex-start"
                  spacing="1rem"
                  style={{ width: '100%', backgroundColor: THEME.color.gainsboro, padding: '0.5rem' }}
                >
                  <Stack.Item style={{ flex: 1 }}>
                    <FormikNumberInput
                      label="Distance parcourue en milles"
                      name="distanceInNauticalMiles"
                      role="distanceInNauticalMiles"
                      isRequired={true}
                      placeholder="0"
                      isLight={true}
                      isErrorMessageHidden={true}
                    />
                  </Stack.Item>
                  <Stack.Item style={{ flex: 1 }}>
                    <FormikNumberInput
                      label="GO marine consommé en litres"
                      name="consumedGOInLiters"
                      role="consumedGOInLiters"
                      isRequired={true}
                      placeholder="0"
                      isLight={true}
                      isErrorMessageHidden={true}
                    />
                  </Stack.Item>
                  <Stack.Item style={{ flex: 1 }}>
                    <FormikNumberInput
                      label="Essence consommée en litres"
                      name="consumedFuelInLiters"
                      role="consumedFuelInLiters"
                      isRequired={true}
                      placeholder="0"
                      isLight={true}
                      isErrorMessageHidden={true}
                    />
                  </Stack.Item>
                </Stack>
              </Stack.Item>

              <Stack.Item style={{ width: '100%' }}>
                <FormikNumberInput
                  isLight={false}
                  isRequired={true}
                  name="nbrOfRecognizedVessel"
                  data-testid="mission-information-general-recognized-vessel"
                  label="Nombre total de navires reconnus dans les approches maritimes ZEE"
                  isErrorMessageHidden={true}
                />
              </Stack.Item>

              <Stack.Item style={{ width: '100%' }}>
                <FormikTextarea
                  isRequired={true}
                  isLight={false}
                  name="observations"
                  data-testid="mission-general-observation"
                  label="Observation générale à l'échelle de la mission (remarques, résumé)"
                  isErrorMessageHidden={true}
                />
              </Stack.Item>
            </Stack>
          </>
        </Formik>
      )}
    </Stack.Item>
  )
}

export default MissionGeneralInformationFormPam
