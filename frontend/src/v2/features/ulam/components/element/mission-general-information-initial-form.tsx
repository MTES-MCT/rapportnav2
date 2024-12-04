import React, { FC } from 'react'
import { FlexboxGrid, Stack } from 'rsuite'
import {
  Accent,
  Button,
  FormikDateRangePicker,
  FormikMultiCheckbox,
  FormikNumberInput,
  FormikSelect
} from '@mtes-mct/monitor-ui'
import {
  MISSION_TYPE_OPTIONS,
  MissionReportTypeEnum,
  MissionULAMGeneralInfoInitial,
  REINFORCEMENT_TYPE,
  REPORT_TYPE_OPTIONS
} from '@common/types/mission-types.ts'
import { useMissionGeneralInformationsForm } from '../../../common/hooks/use-mission-general-informations-form.tsx'
import { FieldProps, Formik } from 'formik'

export interface MissionGeneralInformationInitialFormProps {
  name: string
  fieldFormik: FieldProps<MissionULAMGeneralInfoInitial>
  isCreation?: boolean
  onClose?: () => void
}



const MissionGeneralInformationInitialForm: FC<MissionGeneralInformationInitialFormProps> = ({ name, fieldFormik, isCreation = false, onClose }) => {

  const { initValue, handleSubmit } = useMissionGeneralInformationsForm(name, fieldFormik)



  return (
    <>
      {initValue && (
        <Formik initialValues={initValue} onSubmit={handleSubmit}>
          {formik => (
            <Stack direction="column" spacing="1.5rem" style={{paddingBottom: '2rem'}}>
              <Stack.Item style={{width: '100%'}}>
                <FormikSelect
                  options={REPORT_TYPE_OPTIONS}
                  name="missionReportType"
                  label={"Type de rapport"}
                  isLight
                />
              </Stack.Item >

              <Stack.Item style={{width: '100%', textAlign: 'left'}}>
                <FormikMultiCheckbox
                  label={"Type de mission"}
                  name="missionType"
                  options={MISSION_TYPE_OPTIONS}
                  isInline
                  isLight
                />
              </Stack.Item>

              {formik.values['missionReportType'] === MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT && (
                <Stack.Item style={{width: '100%'}}>
                  <FormikSelect
                    label="Nature du renfort"
                    name="reinforcementType"
                    options={REINFORCEMENT_TYPE}
                    isLight
                  />
                </Stack.Item>
              )}

              <Stack.Item style={{width: '100%', textAlign: 'left'}}>
                <FlexboxGrid>
                  <FlexboxGrid.Item>
                    <FormikDateRangePicker
                      label={"Date et heure de début et de fin"}
                      isLight
                      name="dates"
                    />
                  </FlexboxGrid.Item>

                  {!isCreation && (
                    <FlexboxGrid.Item>
                      <FormikNumberInput label={"Nb d'heures en mer"} isLight name={"nbHourAtSea"}/>
                    </FlexboxGrid.Item>
                  )}
                </FlexboxGrid>


              </Stack.Item>

              {isCreation && (
                <Stack.Item >
                  <Button accent={Accent.PRIMARY} type="submit" onClick={async () => {
                    handleSubmit(formik.values).then(() => onClose())
                  }}>
                    Créer le rapport
                  </Button>

                  <Button accent={Accent.SECONDARY} style={{marginLeft: '10px'}} onClick={onClose}>Annuler</Button>
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
