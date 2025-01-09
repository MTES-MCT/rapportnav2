import { FieldProps } from 'formik'
import { useAbstractFormikSubForm } from './use-abstract-formik-sub-form.tsx'
import { MissionULAMGeneralInfoInitial } from '../types/mission-types.ts'
import { useDate } from './use-date.tsx'

export type MissionULAMGeneralInfoInitialInput = MissionULAMGeneralInfoInitial
export function useMissionGeneralInformationsForm(
  name: string,
  fieldFormik: FieldProps<MissionULAMGeneralInfoInitial>
) {

  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()


  const fromFieldValueToInput = (data: MissionULAMGeneralInfoInitial) => {
    return {
      ...data,
      dates: [
        preprocessDateForPicker(data.startDateTimeUtc),
        preprocessDateForPicker(data.endDateTimeUtc)
      ]
    }
  }


  const fromInputToFieldValue = (value: MissionULAMGeneralInfoInitialInput): MissionULAMGeneralInfoInitial => {
    const { dates, missionReportType, missionTypes, reinforcementType, nbHourAtSea } = value
    return {
      startDateTimeUtc: postprocessDateFromPicker(dates[0]),
      endDateTimeUtc: postprocessDateFromPicker(dates[1]),
      missionTypes,
      missionReportType,
      reinforcementType,
      nbHourAtSea
    }
  }

  const { initValue, handleSubmit } = useAbstractFormikSubForm<MissionULAMGeneralInfoInitial, MissionULAMGeneralInfoInitialInput>(
    name,
    fieldFormik,
    fromFieldValueToInput,
    fromInputToFieldValue
  )


  return {
    initValue,
    handleSubmit,
  }
}
