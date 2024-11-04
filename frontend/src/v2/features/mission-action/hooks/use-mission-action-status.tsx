import { Action, ActionStatus } from '@common/types/action-types'
import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { ActionStatusInput } from '../types/action-type'

export function useMissionActionStatus(
  action: Action,
  onChange: (newAction: Action) => Promise<unknown>
): AbstractFormikSubFormHook<ActionStatusInput> {
  const value = action?.data as unknown as ActionStatus
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: ActionStatus): ActionStatusInput => {
    return { ...data, date: preprocessDateForPicker(data.startDateTimeUtc) }
  }

  const fromInputToFieldValue = (value: ActionStatusInput): ActionStatus => {
    const { date, ...newData } = value
    const startDateTimeUtc = postprocessDateFromPicker(date)
    return { ...newData, startDateTimeUtc }
  }

  const { initValue, handleSubmit, isError } = useAbstractFormik<ActionStatus, ActionStatusInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: ActionStatus) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: [valueToSubmit] })
  }

  const handleSubmitOverride = async (value?: ActionStatusInput, errors?: FormikErrors<ActionStatusInput>) => {
    handleSubmit(value, errors, onSubmit)
  }

  return {
    isError,
    initValue,
    handleSubmit: handleSubmitOverride
  }
}
