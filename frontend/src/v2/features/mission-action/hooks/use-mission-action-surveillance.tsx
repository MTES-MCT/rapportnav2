import { Action } from '@common/types/action-types'
import { EnvActionSurveillance } from '@common/types/env-mission-types'
import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { ActionSurveillanceInput } from '../types/action-type'

export function useMissionActionSurveillance(
  action: Action,
  onChange: (newAction: Action) => Promise<unknown>
): AbstractFormikSubFormHook<ActionSurveillanceInput> {
  const value = action?.data as unknown as EnvActionSurveillance
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: EnvActionSurveillance): ActionSurveillanceInput => {
    const endDate = preprocessDateForPicker(data?.actionEndDateTimeUtc)
    const startDate = preprocessDateForPicker(data.actionStartDateTimeUtc)
    return {
      ...data,
      dates: [startDate, endDate]
    }
  }

  const fromInputToFieldValue = (value: ActionSurveillanceInput): EnvActionSurveillance => {
    const { dates, ...newData } = value
    const actionEndDateTimeUtc = postprocessDateFromPicker(dates[1])
    const actionStartDateTimeUtc = postprocessDateFromPicker(dates[0])
    return { ...newData, actionStartDateTimeUtc, actionEndDateTimeUtc }
  }

  const { initValue, handleSubmit, isError } = useAbstractFormik<EnvActionSurveillance, ActionSurveillanceInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: EnvActionSurveillance) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: [valueToSubmit] })
  }

  const handleSubmitOverride = async (
    value?: ActionSurveillanceInput,
    errors?: FormikErrors<ActionSurveillanceInput>
  ) => {
    handleSubmit(value, errors, onSubmit)
  }

  return {
    isError,
    initValue,
    handleSubmit: handleSubmitOverride
  }
}
