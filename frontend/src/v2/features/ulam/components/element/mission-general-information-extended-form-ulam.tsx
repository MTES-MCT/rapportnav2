import { FormikCheckbox, FormikEffect, FormikTextarea } from '@mtes-mct/monitor-ui'
import { FieldArray, FieldArrayRenderProps, FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { MissionGeneralInfoExtended } from '../../../common/types/mission-types.ts'
import { useUlamMissionGeneralInformationsExtendedForm } from '../../hooks/use-ulam-mission-general-informations-extended-form.tsx'
import MissionGeneralInformationControlUnitRessource from './mission-general-information-control-unit-resource.tsx'
import MissionGeneralInformationCrewUlam from './mission-general-information-crew-ulam.tsx'
import MissionGeneralInformationServiceUlam from './mission-general-information-service-ulam.tsx'

export interface MissionGeneralInformationExtendedFormUlamProps {
  name: string
  missionId?: number
  fieldFormik: FieldProps<MissionGeneralInfoExtended>
}

const MissionGeneralInformationExtendedFormUlam: FC<MissionGeneralInformationExtendedFormUlamProps> = ({
  name,
  missionId,
  fieldFormik
}) => {
  const { initValue, handleSubmit } = useUlamMissionGeneralInformationsExtendedForm(name, fieldFormik)

  return (
    <>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} enableReinitialize={true}>
          {formik => (
            <Stack direction="column" spacing="1em" style={{ width: '100%' }} justifyContent="flex-start">
              <FormikEffect onChange={newValues => handleSubmit(newValues)} />

              <Stack.Item style={{ width: '100%' }}>
                <Stack direction="column" spacing="1em" justifyContent="flex-start" style={{ width: '70%' }}>
                  <Stack.Item style={{ width: '100%' }}>
                    <FieldArray name="resources">
                      {(fieldArray: FieldArrayRenderProps) => (
                        <MissionGeneralInformationControlUnitRessource name="resources" fieldArray={fieldArray} />
                      )}
                    </FieldArray>
                  </Stack.Item>

                  <Stack.Item style={{ width: '100%' }}>
                    <FieldArray name="crew">
                      {(fieldArray: FieldArrayRenderProps) => (
                        <MissionGeneralInformationCrewUlam missionId={missionId} name="crew" fieldArray={fieldArray} />
                      )}
                    </FieldArray>
                  </Stack.Item>

                  <Stack.Item style={{ width: '100%' }}>
                    <FormikCheckbox name={'isMissionArmed'} label={'Mission armée'} />
                  </Stack.Item>

                  <Stack.Item style={{ width: '100%' }}>
                    <FormikCheckbox
                      name={'isWithInterMinisterialService'}
                      label={'Mission conjointe (avec un autre service)'}
                    />
                  </Stack.Item>

                  <Stack.Item style={{ width: '100%', marginBottom: '1.5rem' }}>
                    {formik.values.isWithInterMinisterialService && (
                      <FieldArray name="services">
                        {(fieldArray: FieldArrayRenderProps) => (
                          <MissionGeneralInformationServiceUlam name="services" fieldArray={fieldArray} />
                        )}
                      </FieldArray>
                    )}
                  </Stack.Item>
                </Stack>
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
          )}
        </Formik>
      )}
    </>
  )
}

export default MissionGeneralInformationExtendedFormUlam
