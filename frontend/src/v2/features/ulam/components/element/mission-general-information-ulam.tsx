import { FormikEffect, THEME } from '@mtes-mct/monitor-ui'
import React, { useState } from 'react'
import { useDelay } from '../../../../features/common/hooks/use-delay'
import { Field, FieldProps, Formik } from 'formik'
import {
  MissionGeneralInfoFinal,
  MissionReinforcementTypeEnum,
  MissionReportTypeEnum, MissionType,
  MissionULAMGeneralInfoInitial
} from '../../../common/types/mission-types.ts'
import MissionGeneralInformationInitialForm from './mission-general-information-initial-form.tsx'
import { Stack } from 'rsuite'
import MissionGeneralInformationFinalForm from './mission-general-information-final-form.tsx'

type MissionGeneralInformationUlamProps = {}

type NewMissionUlamGeneralInfoInitial =  { missionGeneralInfo: MissionULAMGeneralInfoInitial }
type NewMissionUlamGeneralInfoFinal = { missionGeneralInfo: MissionGeneralInfoFinal }

const MissionGeneralInformationUlam: React.FC<MissionGeneralInformationUlamProps> = () => {
  const { handleExecuteOnDelay } = useDelay()
  const [value, setValue] = useState<NewMissionUlamGeneralInfoInitial>()
  const handleChange = (nextValue?: NewMissionUlamGeneralInfoInitial) => {
    setValue(nextValue)
    handleExecuteOnDelay(() => console.log(nextValue))
  }

  const initialValues: NewMissionUlamGeneralInfoInitial = {
    missionGeneralInfo : {
      startDateTimeUtc: "",
      endDateTimeUtc: "",
      missionTypes: [MissionType.AIR],
      reinforcementType: MissionReinforcementTypeEnum.DIRM,
      missionReportType: MissionReportTypeEnum.FIELD_REPORT,
      nbHourAtSea: 4
    }
  }

  const finalValues: NewMissionUlamGeneralInfoFinal = {
    missionGeneralInfo: {
      crew: null,
      isJointMission: false,
      isMissionArmed: false,
      resources: null,
      observations: null,
    }
  }

  const handleSubmit = (values: NewMissionUlamGeneralInfoInitial) => {
    console.log('submit')
  }

  const handleSubmitFinal = (values: NewMissionUlamGeneralInfoFinal) => {
    console.log('submitFinal')
  }

  const onClose = () => {
    console.log('onClose')
  }

  return (
    <Stack.Item style={{ backgroundColor: THEME.color.white, width: '100%', padding: '1rem' }}>
      <Stack direction="column" alignItems="flex-start" spacing={'1rem'}>
        <Formik initialValues={initialValues} onSubmit={handleChange}>
          <>
            <FormikEffect onChange={ newValues => handleSubmit(newValues as NewMissionUlamGeneralInfoInitial) } />
            <Field name={"missionGeneralInfo"}>
              {(field: FieldProps<MissionULAMGeneralInfoInitial>) => (
                <MissionGeneralInformationInitialForm
                  name="missionGeneralInfo"
                  fieldFormik={field}
                  isCreation={false}
                  onClose={onClose}
                />
              )}
            </Field>
          </>
        </Formik>
      </Stack>

      <Stack direction="column" alignItems="flex-start" spacing={'1rem'}>
        <Formik initialValues={finalValues} onSubmit={() => console.log('submit final')}>
          <>
            <FormikEffect onChange={ newValues => handleSubmitFinal(newValues as NewMissionUlamGeneralInfoFinal) } />
            <Field name={"missionGeneralInfo"}>
              {(field: FieldProps<MissionGeneralInfoFinal>) => (
                <MissionGeneralInformationFinalForm
                  name="missionGeneralInfo"
                  fieldFormik={field}
                />
              )}
            </Field>
          </>
        </Formik>
      </Stack>
    </Stack.Item>
  )
}

export default MissionGeneralInformationUlam
