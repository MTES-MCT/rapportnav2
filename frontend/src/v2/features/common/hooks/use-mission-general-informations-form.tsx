import {FieldProps} from 'formik'
import {useAbstractFormikSubForm} from './use-abstract-formik-sub-form.tsx'
import {MissionULAMGeneralInfoInitial} from '../types/mission-types.ts'
import {useDate} from './use-date.tsx'

export type MissionULAMGeneralInfoInitialInput = MissionULAMGeneralInfoInitial

export function useMissionGeneralInformationsForm(
  name: string,
  fieldFormik: FieldProps<MissionULAMGeneralInfoInitial>
) {

  const {preprocessDateForPicker, postprocessDateFromPicker} = useDate()


  const fromFieldValueToInput = (data: MissionULAMGeneralInfoInitial) => {
    return {
      ...data,
      startDateTimeUtc: data.startDateTimeUtc && preprocessDateForPicker(data.startDateTimeUtc),
      endDateTimeUtc: data.endDateTimeUtc && preprocessDateForPicker(data.endDateTimeUtc)
    }
  }


  const fromInputToFieldValue = (value: MissionULAMGeneralInfoInitialInput): MissionULAMGeneralInfoInitial => {
    const {missionReportType, missionTypes, reinforcementType, nbHourAtSea, startDateTimeUtc, endDateTimeUtc} = value
    return {
      startDateTimeUtc: postprocessDateFromPicker(startDateTimeUtc),
      endDateTimeUtc: postprocessDateFromPicker(endDateTimeUtc),
      missionTypes,
      missionReportType,
      reinforcementType,
      nbHourAtSea
    }
  }

  const {
    initValue,
    handleSubmit
  } = useAbstractFormikSubForm<MissionULAMGeneralInfoInitial, MissionULAMGeneralInfoInitialInput>(
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
