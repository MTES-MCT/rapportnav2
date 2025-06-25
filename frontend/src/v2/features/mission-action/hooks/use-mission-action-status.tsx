import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionNavActionData } from '../../common/types/mission-action'
import { ActionStatusInput } from '../types/action-type'
import { ActionStatusReason, ActionStatusType } from '@common/types/action-types.ts'
import { mixed, object } from 'yup'

export function useMissionActionStatus(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>
): AbstractFormikSubFormHook<ActionStatusInput> {
  const value = action?.data as MissionNavActionData
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: MissionNavActionData): ActionStatusInput => {
    return { ...data, date: preprocessDateForPicker(data.startDateTimeUtc) }
  }

  const fromInputToFieldValue = (value: ActionStatusInput): MissionNavActionData => {
    const { date, ...newData } = value
    const startDateTimeUtc = postprocessDateFromPicker(date)
    return { ...newData, startDateTimeUtc }
  }

  const { initValue, handleSubmit, isError } = useAbstractFormik<MissionNavActionData, ActionStatusInput>(
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

  const validationSchema = object().shape({
    status: mixed<ActionStatusType>().oneOf(Object.values(ActionStatusType) as ActionStatusType[]),

    reason: mixed<ActionStatusReason>().when('status', {
      is: (val: ActionStatusType) => val === ActionStatusType.DOCKED || val === ActionStatusType.UNAVAILABLE,
      then: schema =>
        schema
          .oneOf(Object.values(ActionStatusReason) as ActionStatusReason[])
          .required('Reason is required when status is DOCKED or UNAVAILABLE'),
      otherwise: schema => schema.notRequired()
    })
  })

  return {
    isError,
    initValue,
    validationSchema,
    handleSubmit: handleSubmitOverride
  }
}
