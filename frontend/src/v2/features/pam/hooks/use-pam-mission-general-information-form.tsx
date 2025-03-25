import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form.tsx'
import {
  MissionGeneralInfo2,
  MissionGeneralInfoExtended,
  MissionGeneralInfoPam,
  MissionULAMGeneralInfoInitial
} from '../../common/types/mission-types.ts'
import { preprocessDateForPicker } from '@common/components/elements/dates/utils.ts'
import { useDate } from '../../common/hooks/use-date.tsx'
import { MissionULAMGeneralInfoInitialInput } from '../../ulam/hooks/use-ulam-mission-general-informations-initial-form.tsx'

export type MissionPAMGeneralInfoInitialInput = { dates: (Date | undefined)[] } & MissionGeneralInfo2

export const usePamMissionGeneralInfoForm = (
  onChange: (newGeneralInfo: MissionGeneralInfo2) => Promise<unknown>,
  value: MissionGeneralInfo2
) => {
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: MissionGeneralInfo2) => {
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    return {
      ...data,
      dates: [startDate, endDate]
    }
  }

  const fromInputToFieldValue = (value: MissionPAMGeneralInfoInitialInput): MissionGeneralInfo2 => {
    const { dates, ...newValues } = value
    return {
      ...newValues,
      startDateTimeUtc: postprocessDateFromPicker(dates[0]),
      endDateTimeUtc: postprocessDateFromPicker(dates[1])
    }
  }

  const { initValue, handleSubmit } = useAbstractFormik<MissionGeneralInfo2, MissionPAMGeneralInfoInitialInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue,
    []
  )

  const onSubmit = async (valueToSubmit?: MissionGeneralInfo2) => {
    if (!valueToSubmit) return
    await onChange(valueToSubmit)
  }

  const handleSubmitOverride = async (value?: MissionGeneralInfo2, errors?: FormikErrors<MissionGeneralInfo2>) => {
    handleSubmit(value, errors, onSubmit)
  }

  return { initValue, handleSubmit: handleSubmitOverride }
}
