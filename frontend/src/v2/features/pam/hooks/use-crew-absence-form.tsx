import { useDate } from '../../common/hooks/use-date.tsx'
import { boolean, mixed, object } from 'yup'
import getDateRangeSchema from '../../common/schemas/dates-schema.ts'
import { useMemo } from 'react'
import { FieldProps } from 'formik'
import { MissionCrewAbsence, MissionCrewAbsenceReason } from '../../common/types/crew-type.ts'
import { useAbstractFormikSubForm } from '../../common/hooks/use-abstract-formik-sub-form.tsx'
import { useMissionDates } from '../../common/hooks/use-mission-dates.tsx'
import { endOfDay, isSameDay, startOfDay } from 'date-fns'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'
import { UTCDate } from '@date-fns/utc'

export type MissionCrewAbsenceInitialInput = { dates: (Date | undefined)[] } & MissionCrewAbsence

export const useCrewAbsenceForm = (name?: string, fieldFormik?: FieldProps<MissionCrewAbsence>, missionId?: string) => {
  const isMissionFinished = useMissionFinished(missionId)
  const missionDates = useMissionDates(missionId)
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()
  const { startDateTimeUtc: missionStartDateTimeUtc, endDateTimeUtc: missionEndDateTimeUtc } =
    useMissionDates(missionId)

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
    const startDate = postprocessDateFromPicker(endOfDay(dates[0]))
    const endDate = postprocessDateFromPicker(startOfDay(dates[1]))

    // if a temporary absence matches the whole length of the mission
    // set it as full mission absence instead of just temporary
    const isAbsentFullMission =
      value.isAbsentFullMission ||
      (missionStartDateTimeUtc &&
        missionEndDateTimeUtc &&
        dates[0] &&
        dates[1] &&
        isSameDay(new UTCDate(dates[0]), new UTCDate(missionStartDateTimeUtc)) &&
        isSameDay(new UTCDate(dates[1]), new UTCDate(missionEndDateTimeUtc)))

    return {
      ...newValues,
      startDate: startDate as unknown as Date,
      endDate: endDate as unknown as Date,
      isAbsentFullMission: !!isAbsentFullMission
    }
  }

  const { initValue, handleSubmit } = useAbstractFormikSubForm<MissionCrewAbsence, MissionCrewAbsenceInitialInput>(
    name,
    fieldFormik,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const createValidationSchema = (isMissionFinished: boolean) => {
    return object().shape({
      ...getDateRangeSchema({
        isMissionFinished,
        missionStartDate: missionDates.startDateTimeUtc
          ? startOfDay(new UTCDate(missionDates.startDateTimeUtc)).toISOString()
          : undefined,
        missionEndDate: missionDates.endDateTimeUtc
          ? endOfDay(new UTCDate(missionDates.endDateTimeUtc)).toISOString()
          : undefined
      }),
      reason: mixed<MissionCrewAbsenceReason>()
        .oneOf(Object.values(MissionCrewAbsenceReason) as MissionCrewAbsenceReason[])
        .required(),
      isAbsentFullMission: boolean().optional()
    })
  }

  const validationSchema = useMemo(
    () => createValidationSchema(isMissionFinished),
    [isMissionFinished, missionDates.startDateTimeUtc, missionDates.endDateTimeUtc]
  )

  return {
    EMPTY_ABSENCE,
    initValue,
    validationSchema,
    handleSubmit
  }
}
