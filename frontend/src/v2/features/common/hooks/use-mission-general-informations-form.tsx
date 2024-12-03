import { FieldProps } from 'formik'
import { postprocessDateFromPicker, preprocessDateForPicker } from '@common/components/elements/dates/utils.ts'
import {
  MissionULAMGeneralInfoInitial, MissionULAMGeneralInfoInitialInput,
} from '@common/types/mission-types.ts'
import { useAbstractFormikSubForm } from './use-abstract-formik-sub-form.tsx'

export function useMissionGeneralInformationsForm(
  name: string,
  fieldFormik: FieldProps<MissionULAMGeneralInfoInitial>
) {


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
    const { dates, missionReportType, missionType, reinforcementType } = value
    return {
      startDateTimeUtc: postprocessDateFromPicker(dates[0]),
      endDateTimeUtc: postprocessDateFromPicker(dates[1]),
      missionType,
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
