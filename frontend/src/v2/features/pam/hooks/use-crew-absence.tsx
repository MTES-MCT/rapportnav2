import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form.tsx'
import { useDate } from '../../common/hooks/use-date.tsx'
import { array, boolean, mixed, number, object } from 'yup'
import getDateRangeSchema from '../../common/schemas/dates-schema.ts'
import { useMemo } from 'react'
import { FieldProps, FormikErrors } from 'formik'
import { ABSENCE_REASON_OPTIONS, MissionCrewAbsence, MissionCrewAbsenceReason } from '../../common/types/crew-type.ts'
import { useMissionDates } from '../../common/hooks/use-mission-dates.tsx'
import { useAbstractFormikSubForm } from '../../common/hooks/use-abstract-formik-sub-form.tsx'
import { UTCDate } from '@date-fns/utc'

export type MissionCrewAbsenceInitialInput = { dates: (Date | undefined)[] } & MissionCrewAbsence

export const useMissionCrewAbsenceForm = (name: string, fieldFormik: FieldProps<MissionCrewAbsence>) => {
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()
  // const missionDates = useMissionDates(value.missionId)

  const EMPTY_ABSENCE: MissionCrewAbsence = {
    id: undefined,
    startDate: undefined,
    endDate: undefined,
    reason: undefined,
    isAbsentFullMission: false
  }

  const ABSENCE_REASON_OPTIONS = [
    { value: 'SICK_LEAVE', label: 'Arrêt maladie' },
    { value: 'TRAINING', label: 'Formation' },
    { value: 'RECOVERING', label: 'Récupération' },
    { value: 'HOLIDAYS', label: 'Congès' },
    { value: 'MEETING', label: 'Réunion' },
    { value: 'DISPATCHED_ELSEWHERE', label: 'Renforcement extérieur' },
    { value: 'OTHER', label: 'Autre' }
  ]

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
      reason: mixed<MissionCrewAbsenceReason>().oneOf(
        Object.values(MissionCrewAbsenceReason) as MissionCrewAbsenceReason[]
      ),
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
