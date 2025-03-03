import { FormikEffect, FormikMultiCheckbox, FormikNumberInput, FormikSelect } from '@mtes-mct/monitor-ui'
import { Field, FieldProps, Formik } from 'formik'
import { FC } from 'react'
import { Stack } from 'rsuite'
import { FormikDateRangePicker } from '../../../common/components/ui/formik-date-range-picker.tsx'
import { useMissionType } from '../../../common/hooks/use-mission-type.tsx'
import { MissionReportTypeEnum, MissionULAMGeneralInfoInitial } from '../../../common/types/mission-types.ts'
import {
  MissionULAMGeneralInfoInitialInput,
  useUlamMissionGeneralInformationInitialForm
} from '../../hooks/use-ulam-mission-general-informations-initial-form.tsx'

export interface MissionGeneralInformationInitialFormUlamProps {
  name: string
  isCreation?: boolean
  fieldFormik: FieldProps<MissionULAMGeneralInfoInitial>
}

const MissionGeneralInformationInitialFormUlam: FC<MissionGeneralInformationInitialFormUlamProps> = ({
  name,
  isCreation,
  fieldFormik
}) => {
  const { missionTypeOptions, reportTypeOptions, reinforcementTypeOptions } = useMissionType()
  const { initValue, handleSubmit, validationSchema } = useUlamMissionGeneralInformationInitialForm(name, fieldFormik)

  return (
    <>
      {initValue && (
        <Formik
          initialValues={initValue}
          onSubmit={handleSubmit}
          validateOnChange
          enableReinitialize
          validationSchema={validationSchema}
        >
          {({ values, validateForm }) => (
            <Stack direction="column">
              <FormikEffect
                onChange={async nextValue =>
                  validateForm(nextValue).then(errors =>
                    handleSubmit(nextValue as MissionULAMGeneralInfoInitialInput, errors)
                  )
                }
              />
              <Stack.Item style={{ width: '100%', marginBottom: '1em' }}>
                <FormikSelect
                  isLight={isCreation}
                  isRequired={true}
                  disabled={!isCreation}
                  name="missionReportType"
                  label={'Type de rapport'}
                  options={reportTypeOptions}
                />
              </Stack.Item>

              <Stack.Item style={{ width: '100%', marginBottom: '1em', textAlign: 'left' }}>
                <FormikMultiCheckbox
                  isInline
                  isLight
                  isRequired={true}
                  name="missionTypes"
                  label={'Type de mission'}
                  options={missionTypeOptions}
                />
              </Stack.Item>

              {values.missionReportType === MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT && (
                <Stack.Item style={{ width: '100%', marginBottom: '1em' }}>
                  <FormikSelect
                    isLight={isCreation}
                    label="Nature du renfort"
                    name="reinforcementType"
                    options={reinforcementTypeOptions}
                  />
                </Stack.Item>
              )}

              <Stack.Item style={{ width: '100%', marginBottom: '1em', textAlign: 'left' }}>
                <Stack direction="row">
                  <Stack.Item style={{ width: '70%' }}>
                    <Field name="dates">
                      {(field: FieldProps<Date[]>) => (
                        <FormikDateRangePicker
                          label=""
                          name="dates"
                          isLight={isCreation}
                          fieldFormik={field}
                          validateOnSubmit={isCreation}
                        />
                      )}
                    </Field>
                  </Stack.Item>
                  <Stack.Item style={{ width: '30%' }}>
                    {!isCreation && <FormikNumberInput label={"Nb d'heures en mer"} name="nbHourAtSea" />}
                  </Stack.Item>
                </Stack>
              </Stack.Item>
            </Stack>
          )}
        </Formik>
      )}
    </>
  )
}

export default MissionGeneralInformationInitialFormUlam
