import { MissionTypeEnum } from '@common/types/env-mission-types.ts'
import * as Yup from 'yup'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form.tsx'
import { useDate } from '../../common/hooks/use-date.tsx'
import {
  MissionGeneralInfo2,
  MissionReinforcementTypeEnum,
  MissionReportTypeEnum
} from '../../common/types/mission-types.ts'

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
    })
  })

  const handleSubmitOverride = async (value?: MissionGeneralInfoInput) => {
    handleSubmit(value, onSubmit)
  }

  return { initValue, handleSubmit: handleSubmitOverride, validationSchema }
}
