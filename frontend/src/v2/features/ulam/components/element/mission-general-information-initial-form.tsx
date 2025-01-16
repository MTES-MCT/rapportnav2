import {
  Accent,
  Button,
  FormikDateRangePicker,
  FormikMultiCheckbox,
  FormikNumberInput,
  FormikSelect, Icon, IconButton, Size, THEME
} from '@mtes-mct/monitor-ui'
import { FieldProps, Formik } from 'formik'
import React, { FC } from 'react'
import { FlexboxGrid, Stack } from 'rsuite'
import { useMissionGeneralInformationsForm } from '../../../common/hooks/use-mission-general-informations-form.tsx'
import { useMissionType } from '../../../common/hooks/use-mission-type.tsx'
import { MissionReportTypeEnum, MissionULAMGeneralInfoInitial } from '../../../common/types/mission-types.ts'

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

  return (
    <>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit}>
          {formik => (
            <Stack direction="column" spacing="1.5rem" style={{ paddingBottom: '2rem' }}>
              <Stack.Item style={{ width: '100%' }}>
                <FormikSelect options={reportTypeOptions} name="missionReportType" label={'Type de rapport'} isLight />
              </Stack.Item>

              <Stack.Item style={{ width: '100%', textAlign: 'left' }}>
                <FormikMultiCheckbox
                  label={'Type de mission'}
                  name="missionTypes"
                  options={missionTypeOptions}
                  isInline
                  isLight
                />
              </Stack.Item>

              {formik.values['missionReportType'] === MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT && (
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
                <FlexboxGrid>
                  <FlexboxGrid.Item>
                    <FormikDateRangePicker
                      label={'Dates et heures de début et de fin du rapport'}
                      isLight name="dates"
                      withTime={true}
                    />
                  </FlexboxGrid.Item>

                  {!isCreation && (
                    <FlexboxGrid.Item>
                      <FormikNumberInput label={"Nb d'heures en mer"} isLight name={'nbHourAtSea'} />
                    </FlexboxGrid.Item>
                  )}
                </FlexboxGrid>
              </Stack.Item>

              {isCreation && (
                <Stack.Item>
                  <Button
                    accent={Accent.PRIMARY}
                    type="submit"
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
