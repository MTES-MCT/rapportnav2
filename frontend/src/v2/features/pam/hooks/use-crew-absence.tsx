import { useDate } from '../../common/hooks/use-date.tsx'
import { boolean, mixed, object } from 'yup'
import getDateRangeSchema from '../../common/schemas/dates-schema.ts'
import { useMemo } from 'react'
import { FieldProps } from 'formik'
import { ABSENCE_REASON_OPTIONS, MissionCrewAbsence, MissionCrewAbsenceReason } from '../../common/types/crew-type.ts'
import { useAbstractFormikSubForm } from '../../common/hooks/use-abstract-formik-sub-form.tsx'
import { useMissionDates } from '../../common/hooks/use-mission-dates.tsx'
import { isSameDay } from 'date-fns'

export type MissionCrewAbsenceInitialInput = { dates: (Date | undefined)[] } & MissionCrewAbsence

export const useMissionCrewAbsenceForm = (
  name: string,
  fieldFormik: FieldProps<MissionCrewAbsence>,
  missionId?: string
) => {
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()
  const [missionStartDateTimeUtc, missionEndDateTimeUtc] = useMissionDates(missionId)

  const EMPTY_ABSENCE: MissionCrewAbsence = {
    id: undefined,
    startDate: undefined,
    endDate: undefined,
    reason: undefined,
    isAbsentFullMission: false
  }

  const fromFieldValueToInput = (data: MissionCrewAbsence) => {
    const startDate = preprocessDateForPicker(data.startDate as unknown as string)
    const endDate = preprocessDateForPicker(data.endDate as unknown as string)
    return {
      ...data,
      dates: [startDate, endDate]
    }
  }

  const fromInputToFieldValue = (value: MissionCrewAbsenceInitialInput): MissionCrewAbsence => {
    const { dates, ...newValues } = value
    const startDate = postprocessDateFromPicker(dates[0])
    const endDate = postprocessDateFromPicker(dates[1])

    // if a temporary absence matches the whole length of the mission
    // set it as full mission absence instead of just temporary
    const isAbsentFullMission =
      value.isAbsentFullMission ||
      (missionStartDateTimeUtc &&
        missionEndDateTimeUtc &&
        startDate &&
        endDate &&
        isSameDay(new Date(startDate), new Date(missionStartDateTimeUtc)) &&
        isSameDay(new Date(endDate), new Date(missionEndDateTimeUtc)))

    return {
      ...newValues,
      startDate: startDate as unknown as Date,
      endDate: endDate as unknown as Date,
      isAbsentFullMission: !!isAbsentFullMission
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
