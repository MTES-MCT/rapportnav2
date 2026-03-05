import { MissionTypeEnum } from '@common/types/env-mission-types.ts'
import { FormikErrors } from 'formik'
import * as Yup from 'yup'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form.tsx'
import { useDate } from '../../common/hooks/use-date.tsx'
import {
  MissionGeneralInfo2,
  MissionReinforcementTypeEnum,
  MissionReportTypeEnum
} from '../../common/types/mission-types.ts'
import getDateRangeSchema from '../../common/schemas/dates-schema.ts'

export type MissionGeneralInfoInput = {
  dates: any
} & MissionGeneralInfo2

export const useUlamMissionGeneralInfoForm = (
  onChange: (newGeneralInfo: MissionGeneralInfo2) => Promise<unknown>,
  value: MissionGeneralInfo2
) => {
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()
  const fromFieldValueToInput = (data: MissionGeneralInfo2): MissionGeneralInfoInput => {
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    return {
      ...data,
      dates: [startDate, endDate],
      resources: data.resources ?? []
    }
  }

  const fromInputToFieldValue = (value: MissionGeneralInfoInput): MissionGeneralInfo2 => {
    const { dates, ...newValues } = value
    return {
      ...newValues,
      startDateTimeUtc: postprocessDateFromPicker(dates[0]),
      endDateTimeUtc: postprocessDateFromPicker(dates[1]),
      resources: value.isResourcesNotUsed ? [] : value.resources,
      interMinisterialServices: !value.isWithInterMinisterialService ? [] : value.interMinisterialServices
    }
  }

  const { initValue, handleSubmit } = useAbstractFormik<MissionGeneralInfo2, MissionGeneralInfoInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue,
    [
      'isMissionArmed',
      'isAllAgentsParticipating',
      'isWithInterMinisterialService',
      'isUnderJdp',
      'isMissionNav',
      'isDeleted',
      'isResourcesNotUsed'
    ]
  )

  const onSubmit = async (valueToSubmit?: MissionGeneralInfo2) => {
    if (!valueToSubmit) return
    await onChange(valueToSubmit)
  }

  const validationSchema = Yup.object().shape({
    ...getDateRangeSchema({ isMissionFinished: true }),
    missionReportType: Yup.mixed<MissionReportTypeEnum>().required('Type de rapport obligatoire'),
    reinforcementType: Yup.mixed<MissionReinforcementTypeEnum>().when('missionReportType', {
      is: MissionReportTypeEnum.EXTERNAL_REINFORCEMENT_TIME_REPORT,
      then: schema => schema.required('Nature du renfort obligatoire')
    }),
    missionTypes: Yup.mixed<MissionTypeEnum[]>().when('missionReportType', {
      is: MissionReportTypeEnum.FIELD_REPORT,
      then: schema => schema.required('Type de mission obligatoire')
    })
  })

  const handleSubmitOverride = async (
    value?: MissionGeneralInfoInput,
    errors?: FormikErrors<MissionGeneralInfoInput>
  ) => {
    handleSubmit(value, errors, onSubmit)
  }

  return { initValue, handleSubmit: handleSubmitOverride, validationSchema }
}
