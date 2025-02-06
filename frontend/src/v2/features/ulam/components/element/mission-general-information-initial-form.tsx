import {
  Accent,
  Button,
  FormikDateRangePicker, FormikEffect,
  FormikMultiCheckbox,
  FormikNumberInput,
  FormikSelect, Icon, Size
} from '@mtes-mct/monitor-ui'
import { FieldProps, Formik } from 'formik'
import { FC, useEffect } from 'react'
import { FlexboxGrid, Stack } from 'rsuite'
import { useMissionGeneralInformationsForm } from '../../../common/hooks/use-mission-general-informations-form.tsx'
import { useMissionType } from '../../../common/hooks/use-mission-type.tsx'
import {
  MissionReinforcementTypeEnum,
  MissionReportTypeEnum,
  MissionULAMGeneralInfoInitial
} from '../../../common/types/mission-types.ts'
import * as Yup from 'yup'

export interface MissionGeneralInformationInitialFormProps {
  name: string
  fieldFormik: FieldProps<MissionULAMGeneralInfoInitial>
  isCreation?: boolean
  onClose?: () => void
}

const MissionGeneralInformationInitialForm: FC<MissionGeneralInformationInitialFormProps> = ({
                                                                                               name,
                                                                                               fieldFormik,
                                                                                               isCreation = false,
                                                                                               onClose
                                                                                             }) => {
  const { initValue, handleSubmit } = useMissionGeneralInformationsForm(name, fieldFormik)
  const { missionTypeOptions, reportTypeOptions, reinforcementTypeOptions } = useMissionType()

  const generalInfoInitialSchema = Yup.object().shape({
    missionReportType: Yup.mixed<MissionReportTypeEnum>().required('Type de rapport obligatoire'),
    missionTypes: Yup.array().required('Type de mission obligatoire'),
    dates: Yup.array().required('Date et heure de début et de fin obligatoire'),
    reinforcementType: Yup.mixed<MissionReinforcementTypeEnum>().when("missionReportType", {
      is: MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT,
      then: (schema) => schema.required("Nature du renfort obligatoire")
    })
  });


  return (
    <>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit} validationSchema={generalInfoInitialSchema} validateOnMount>
          {(formik) => (
            <Stack direction="column" spacing="1.5rem" style={{ paddingBottom: '2rem' }}>
              <FormikEffect onChange={newValues => handleSubmit(newValues)} />
              <Stack.Item style={{ width: '100%' }}>
                <FormikSelect
                  options={reportTypeOptions}
                  name="missionReportType"
                  label={'Type de rapport'}
                  isLight
                  isRequired={true}
                  disabled={!isCreation}
                />
              </Stack.Item>

              <Stack.Item style={{ width: '100%', textAlign: 'left' }}>
                <FormikMultiCheckbox
                  label={'Type de mission'}
                  name="missionTypes"
                  options={missionTypeOptions}
                  isInline
                  isLight
                  isRequired={true}
                />
              </Stack.Item>

              {formik.values.missionReportType === MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT && (
                <Stack.Item style={{ width: '100%' }}>
                  <FormikSelect
                    label="Nature du renfort"
                    name="reinforcementType"
                    options={reinforcementTypeOptions}
                    isLight
                  />
                </Stack.Item>
              )}

              <Stack.Item style={{ width: '100%', textAlign: 'left' }}>
                <FlexboxGrid justify={'space-between'}>
                  <FlexboxGrid.Item>
                    <FormikDateRangePicker
                      label={'Dates et heures de début et de fin du rapport'}
                      isLight={isCreation}
                      name="dates"
                      withTime={true}
                      isCompact={true}
                    />
                  </FlexboxGrid.Item>

                  {!isCreation && (
                      <FlexboxGrid.Item>
                        <FormikNumberInput
                          label={"Nb d'heures en mer"}
                          name={'nbHourAtSea'}
                        />
                      </FlexboxGrid.Item>
                  )}
                </FlexboxGrid>
              </Stack.Item>

              {isCreation && (
                <Stack.Item>
                  <Button
                    accent={Accent.PRIMARY}
                    type="submit"
                    disabled={!formik.isValid || formik.isSubmitting}
                    onClick={async () => {
                      handleSubmit(formik.values)
                    }}
                  >
                    Créer le rapport
                  </Button>

                  <Button accent={Accent.SECONDARY} style={{ marginLeft: '10px' }} onClick={onClose}>
                    Annuler
                  </Button>
                </Stack.Item>
              )}
            </Stack>
          )}
        </Formik>
      )}
    </>
  )
}

export default MissionGeneralInformationInitialForm


