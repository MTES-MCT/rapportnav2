import { FieldProps } from 'formik'
import { MissionULAMGeneralInfoInitial } from '../types/mission-types.ts'
import { useAbstractFormikSubForm } from './use-abstract-formik-sub-form.tsx'
import { useDate } from './use-date.tsx'

export type MissionULAMGeneralInfoInitialInput = { dates: Date[] } & MissionULAMGeneralInfoInitial

export function useMissionGeneralInformationsForm(
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
    const { missionReportType, missionTypes, reinforcementType, nbHourAtSea, dates } = value
    return {
      startDateTimeUtc: postprocessDateFromPicker(dates[0]),
      endDateTimeUtc: postprocessDateFromPicker(dates[1]),
      missionTypes,
      missionReportType,
      reinforcementType,
      nbHourAtSea
    }
  }

  const { initValue, handleSubmit } = useAbstractFormikSubForm<
    MissionULAMGeneralInfoInitial,
    MissionULAMGeneralInfoInitialInput
  >(name, fieldFormik, fromFieldValueToInput, fromInputToFieldValue)

  return {
    initValue,
    handleSubmit
  }
}
