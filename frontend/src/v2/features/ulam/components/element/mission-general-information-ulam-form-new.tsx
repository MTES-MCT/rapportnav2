import React from 'react'
import { MissionTypeEnum } from '@common/types/env-mission-types.ts'
import {
  MissionReinforcementTypeEnum,
  MissionReportTypeEnum,
  MissionULAMGeneralInfoInitial
} from '@common/types/mission-types.ts'
import { Field, FieldProps, Formik } from 'formik'
import MissionGeneralInformationInitialForm from './mission-general-information-initial-form.tsx'
import { Accent, Button, Dialog, FormikEffect, THEME } from '@mtes-mct/monitor-ui'

type NewMissionUlamGeneralInfoInitial =  { missionGeneralInfo: MissionULAMGeneralInfoInitial }

export interface MissionGeneralInformationUlamProps {
  startDateTimeUtc: string
  endDateTimeUtc: string
  missionType: MissionTypeEnum[]
  missionReportType?: MissionReportTypeEnum
  reinforcementType?: MissionReinforcementTypeEnum
  nbHourAtSea?: number
  onClose?: () => void
}

const MissionGeneralInformationUlamFormNew: React.FC<MissionGeneralInformationUlamProps> = ({ startDateTimeUtc, endDateTimeUtc, missionType, missionReportType, reinforcementType, nbHourAtSea, onClose }) => {

  const initialValues: MissionULAMGeneralInfoInitial = {
    missionGeneralInfo : {
      startDateTimeUtc,
      endDateTimeUtc,
      missionType,
      reinforcementType,
      missionReportType,
      nbHourAtSea
    }
  }


  const handleSubmit = (values) => {
    if (
      values?.missionGeneralInfo?.missionReportType !== MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT
    ) {
      values.missionGeneralInfo = values.missionGeneralInfo || {}
      values.missionGeneralInfo.reinforcementType = null;
    }
    console.log('Form Submitted:', values);
  };


  return (
    <>
      <Formik initialValues={initialValues} onSubmit={handleSubmit}>
        <>
          <FormikEffect onChange={newValues => handleSubmit(newValues as NewMissionUlamGeneralInfoInitial)} />
          <Field name={"missionGeneralInfo"}>
            {(field: FieldProps<MissionULAMGeneralInfoInitial>) => (
              <MissionGeneralInformationInitialForm
                name="missionGeneralInfo"
                fieldFormik={field}
                isCreation={true}
                onClose={onClose}
              />
            )}
          </Field>
        </>
      </Formik>

    </>

  )
}

export default MissionGeneralInformationUlamFormNew
