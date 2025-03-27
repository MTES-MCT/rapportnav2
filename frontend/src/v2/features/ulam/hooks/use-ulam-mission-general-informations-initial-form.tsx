import { FieldProps } from 'formik'
import * as Yup from 'yup'
import { useAbstractFormikSubForm } from '../../common/hooks/use-abstract-formik-sub-form.tsx'
import { useDate } from '../../common/hooks/use-date.tsx'
import {
  MissionReinforcementTypeEnum,
  MissionReportTypeEnum,
  MissionULAMGeneralInfoInitial
} from '../../common/types/mission-types.ts'
import { MissionTypeEnum } from '@common/types/env-mission-types.ts'

export type MissionULAMGeneralInfoInitialInput = { dates: (Date | undefined)[] } & MissionULAMGeneralInfoInitial

export function useUlamMissionGeneralInformationInitialForm(
  name: string,
  fieldFormik: FieldProps<MissionULAMGeneralInfoInitial>
) {
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: MissionULAMGeneralInfoInitial) => {
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    return {
      ...data,
      dates: [startDate, endDate]
    }
  }

  const fromInputToFieldValue = (value: MissionULAMGeneralInfoInitialInput): MissionULAMGeneralInfoInitial => {
    const { dates, ...newValues } = value
    return {
      ...newValues,
      startDateTimeUtc: postprocessDateFromPicker(dates[0]),
      endDateTimeUtc: postprocessDateFromPicker(dates[1])
    }
  }

  const { initValue, handleSubmit } = useAbstractFormikSubForm<
    MissionULAMGeneralInfoInitial,
    MissionULAMGeneralInfoInitialInput
  >(name, fieldFormik, fromFieldValueToInput, fromInputToFieldValue)

  const validationSchema = Yup.object().shape({
    missionReportType: Yup.mixed<MissionReportTypeEnum>().required('Type de rapport obligatoire'),
    dates: Yup.array()
      .of(Yup.date())
      .test(value => value && value[0] && value[1] && new Date(value[1]) > new Date(value[0]))
      .required('Date et heure de début et de fin obligatoire'),
    reinforcementType: Yup.mixed<MissionReinforcementTypeEnum>().when('missionReportType', {
      is: MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT,
      then: schema => schema.required('Nature du renfort obligatoire')
    }),
    missionTypes: Yup.mixed<MissionTypeEnum[]>().when('missionReportType', {
      is: MissionReportTypeEnum.FIELD_REPORT,
      then: schema => schema.required('Type de mission obligatoire')
    }),
  })

  return {
    initValue,
    handleSubmit,
    validationSchema
  }
}
