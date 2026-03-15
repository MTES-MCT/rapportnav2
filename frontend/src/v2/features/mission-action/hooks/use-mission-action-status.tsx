import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionNavActionData } from '../../common/types/mission-action'
import { ActionStatusInput } from '../types/action-type'
import { ActionStatusReason, ActionStatusType } from '@common/types/action-types.ts'
import { mixed, object } from 'yup'
import { useMemo } from 'react'
import conditionallyRequired from '../../common/schemas/conditionally-required-helper.ts'
import { getSingleDateSchema } from '../../common/schemas/dates-schema.ts'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'
import { useMissionDates } from '../../common/hooks/use-mission-dates.tsx'

export function useMissionActionStatus(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>
): AbstractFormikSubFormHook<ActionStatusInput> {
  const value = action?.data as MissionNavActionData
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()
  const missionDates = useMissionDates(action.ownerId ?? action.missionId)
  const isMissionFinished = useMissionFinished(action.ownerId ?? action.missionId)

  const fromFieldValueToInput = (data: MissionNavActionData): ActionStatusInput => {
    return { ...data, date: preprocessDateForPicker(data.startDateTimeUtc) }
  }

  const fromInputToFieldValue = (value: ActionStatusInput): MissionNavActionData => {
    const { date, ...newData } = value
    const startDateTimeUtc = postprocessDateFromPicker(date)
    return { ...newData, startDateTimeUtc }
  }

  const { initValue, handleSubmit, errors } = useAbstractFormik<MissionNavActionData, ActionStatusInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: MissionNavActionData) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
  }

  const handleSubmitOverride = async (value?: ActionStatusInput, errors?: FormikErrors<ActionStatusInput>) => {
    handleSubmit(value, errors, onSubmit)
  }

  const createValidationSchema = (isMissionFinished: boolean, missionStartDate?: string, missionEndDate?: string) => {
    return object().shape({
      status: mixed<ActionStatusType>().oneOf(Object.values(ActionStatusType) as ActionStatusType[]),
      reason: conditionallyRequired(
        () => mixed<ActionStatusReason>(),
        'status',
        (val: ActionStatusType) => val === ActionStatusType.DOCKED || val === ActionStatusType.UNAVAILABLE,
        'Reason is required when status is DOCKED or UNAVAILABLE',
        schema => schema.oneOf(Object.values(ActionStatusReason) as ActionStatusReason[])
      )(isMissionFinished),
      date: getSingleDateSchema({ isMissionFinished, missionStartDate, missionEndDate })
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
