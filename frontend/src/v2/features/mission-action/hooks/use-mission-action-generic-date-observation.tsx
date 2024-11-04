import { Action } from '@common/types/action-types'
import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { ActionGenericDateObservation, ActionGenericDateObservationInput } from '../types/action-type'

export function useMissionActionGenericDateObservation(
  action: Action,
  onChange: (newAction: Action) => Promise<unknown>
): AbstractFormikSubFormHook<ActionGenericDateObservationInput> {
  const value = action?.data as unknown as ActionGenericDateObservation
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: ActionGenericDateObservation): ActionGenericDateObservationInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    return { ...data, dates: [startDate, endDate] }
  }

  const fromInputToFieldValue = (value: ActionGenericDateObservationInput): ActionGenericDateObservation => {
    const { dates, ...newData } = value
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    return { ...newData, startDateTimeUtc, endDateTimeUtc }
  }

  const { initValue, handleSubmit, isError } = useAbstractFormik<
    ActionGenericDateObservation,
    ActionGenericDateObservationInput
  >(value, fromFieldValueToInput, fromInputToFieldValue)

  const onSubmit = async (valueToSubmit?: ActionGenericDateObservation) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: [valueToSubmit] })
  }

  const handleSubmitOverride = async (
    value?: ActionGenericDateObservationInput,
    errors?: FormikErrors<ActionGenericDateObservationInput>
  ) => {
    handleSubmit(value, errors, onSubmit)
  }

  return {
    isError,
    initValue,
    handleSubmit: handleSubmitOverride
  }
}
