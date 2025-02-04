import React from 'react'
import {Field, FieldProps, Formik} from 'formik'
import MissionGeneralInformationInitialForm from './mission-general-information-initial-form.tsx'
import {FormikEffect} from '@mtes-mct/monitor-ui'
import {
  MissionReinforcementTypeEnum,
  MissionReportTypeEnum,
  MissionType,
  MissionULAMGeneralInfoInitial
} from '../../../common/types/mission-types.ts'
import useCreateMissionMutation from '../../services/use-create-mission.tsx'
import {useNavigate} from 'react-router-dom'

type NewMissionUlamGeneralInfoInitial = { missionGeneralInfo: MissionULAMGeneralInfoInitial }

export interface MissionGeneralInformationUlamProps {
  startDateTimeUtc?: string
  endDateTimeUtc?: string
  missionTypes?: MissionType[]
  missionReportType?: MissionReportTypeEnum
  reinforcementType?: MissionReinforcementTypeEnum
  nbHourAtSea?: number
  onClose?: () => void
}

const MissionGeneralInformationUlamFormNew: React.FC<MissionGeneralInformationUlamProps> = ({
                                                                                              startDateTimeUtc,
                                                                                              endDateTimeUtc,
                                                                                              missionTypes,
                                                                                              missionReportType,
                                                                                              reinforcementType,
                                                                                              nbHourAtSea,
                                                                                              onClose
                                                                                            }) => {

  const navigate = useNavigate()

  const initialValues: NewMissionUlamGeneralInfoInitial = {
    missionGeneralInfo: {
      startDateTimeUtc,
      endDateTimeUtc,
      missionTypes,
      reinforcementType,
      missionReportType,
      nbHourAtSea
    }
  }

  const mutation = useCreateMissionMutation();

  const handleSubmit = (values: NewMissionUlamGeneralInfoInitial) => {
    const {missionGeneralInfo} = values || {};

    if (missionGeneralInfo?.missionReportType !== MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT) {
      values.missionGeneralInfo = {
        ...missionGeneralInfo,
        reinforcementType: undefined,
      };
    }

    const hasMissionTypes = Array.isArray(missionGeneralInfo?.missionTypes) && missionGeneralInfo.missionTypes.length > 0;

    if (hasMissionTypes) {
      mutation.mutateAsync(missionGeneralInfo).then((r) => navigate(`/v2/ulam/missions/${r.id}`));
    }
  };


  return (
    <>
      <Formik initialValues={initialValues} onSubmit={handleSubmit}>
        <>
          <FormikEffect onChange={newValues => handleSubmit(newValues as NewMissionUlamGeneralInfoInitial)}/>
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
