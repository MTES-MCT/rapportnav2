import { FormikErrors } from 'formik'
import { useMemo } from 'react'
import { object, ObjectShape } from 'yup'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'
import { useMissionDates } from '../../common/hooks/use-mission-dates.tsx'
import getDateRangeSchema from '../../common/schemas/dates-schema.ts'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction } from '../../common/types/mission-action'
import { MissionActionData } from '../../common/types/mission-action-data'
import { ActionGenericDateObservationInput } from '../types/action-type'

export function useMissionActionGenericDateObservation(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>,
  schema?: ObjectShape
): AbstractFormikSubFormHook<ActionGenericDateObservationInput> {
  const { getDateRangeForInput, getDateRangeFromInput } = useDate()
  const isMissionFinished = useMissionFinished(action.ownerId ?? action.missionId)
  const missionDates = useMissionDates(action.ownerId ?? action.missionId)

  const fromFieldValueToInput = (data: MissionActionData): ActionGenericDateObservationInput => {
    const dates = getDateRangeForInput(data)
    return { ...data, dates }
  }

  const fromInputToFieldValue = (value: ActionGenericDateObservationInput): MissionActionData => {
    const { dates, ...newData } = value
    return { ...newData, ...getDateRangeFromInput(dates) }
  }

  const { initValue, handleSubmit, errors } = useAbstractFormik<MissionActionData, ActionGenericDateObservationInput>(
    action.data,
    fromFieldValueToInput,
    fromInputToFieldValue,
    ['isWithinDepartment', 'hasDivingDuringOperation']
  )

  const onSubmit = async (valueToSubmit?: MissionActionData) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
  }

  const handleSubmitOverride = async (
    value?: ActionGenericDateObservationInput,
    errors?: FormikErrors<ActionGenericDateObservationInput>
  ) => {
    handleSubmit(value, errors, onSubmit)
  }

  const createValidationSchema = (isMissionFinished: boolean, missionStartDate?: string, missionEndDate?: string) => {
    return object().shape({
      ...(getDateRangeSchema({ isMissionFinished, missionStartDate, missionEndDate }) as Record<string, any>),
      ...(schema ?? {})
    })
  }

  const validationSchema = useMemo(
    () => createValidationSchema(isMissionFinished, missionDates.startDateTimeUtc, missionDates.endDateTimeUtc),
    [isMissionFinished, missionDates.startDateTimeUtc, missionDates.endDateTimeUtc]
  )

  return {
    errors,
    initValue,
    validationSchema,
    handleSubmit: handleSubmitOverride
  }
}
