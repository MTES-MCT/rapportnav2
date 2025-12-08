import { useDate } from '../../common/hooks/use-date.tsx'
import { boolean, mixed, object } from 'yup'
import getDateRangeSchema from '../../common/schemas/dates-schema.ts'
import { useMemo } from 'react'
import { FieldProps } from 'formik'
import { ABSENCE_REASON_OPTIONS, MissionCrewAbsence, MissionCrewAbsenceReason } from '../../common/types/crew-type.ts'
import { useAbstractFormikSubForm } from '../../common/hooks/use-abstract-formik-sub-form.tsx'
import { useMissionDates } from '../../common/hooks/use-mission-dates.tsx'

export type MissionCrewAbsenceInitialInput = { dates: (Date | undefined)[] } & MissionCrewAbsence

export const useMissionCrewAbsenceForm = (name: string, fieldFormik: FieldProps<MissionCrewAbsence>) => {
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const EMPTY_ABSENCE: MissionCrewAbsence = {
    id: undefined,
    startDate: undefined,
    endDate: undefined,
    reason: undefined,
    isAbsentFullMission: false
  }

  const fromFieldValueToInput = (data: MissionCrewAbsence) => {
    const startDate = preprocessDateForPicker(data.startDate)
    const endDate = preprocessDateForPicker(data.endDate)
    return {
      ...data,
      dates: [startDate, endDate]
    }
  }

  const fromInputToFieldValue = (value: MissionCrewAbsenceInitialInput): MissionCrewAbsence => {
    const { dates, ...newValues } = value
    return {
      ...newValues,
      startDate: postprocessDateFromPicker(dates[0]),
      endDate: postprocessDateFromPicker(dates[1])
    }
  }

  const { initValue, handleSubmit, errors } = useAbstractFormikSubForm<
    MissionCrewAbsence,
    MissionCrewAbsenceInitialInput
  >(name, fieldFormik, fromFieldValueToInput, fromInputToFieldValue)

  const createValidationSchema = () => {
    return object().shape({
      ...getDateRangeSchema(false),
      reason: mixed<MissionCrewAbsenceReason>()
        .oneOf(Object.values(MissionCrewAbsenceReason) as MissionCrewAbsenceReason[])
        .required(),
      isAbsentFullMission: boolean().optional()
    })
  }

  const validationSchema = useMemo(() => createValidationSchema(), [])

  return {
    EMPTY_ABSENCE,
    ABSENCE_REASON_OPTIONS,
    errors,
    initValue,
    validationSchema,
    handleSubmit
  }
}
