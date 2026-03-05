import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionEnvActionData } from '../../common/types/mission-action'
import { ActionSurveillanceInput } from '../types/action-type'

export function useMissionActionSurveillance(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>
): AbstractFormikSubFormHook<ActionSurveillanceInput> {
  const value = action?.data as unknown as MissionEnvActionData
  const { getDateRangeForInput, getDateRangeFromInput } = useDate()

  const fromFieldValueToInput = (data: MissionEnvActionData): ActionSurveillanceInput => {
    const dates = getDateRangeForInput(data)
    return { ...data, dates }
  }

  const fromInputToFieldValue = (value: ActionSurveillanceInput): MissionEnvActionData => {
    const { dates, ...newData } = value
    return { ...newData, ...getDateRangeFromInput(dates) }
  }

  const { initValue, handleSubmit, errors } = useAbstractFormik<MissionEnvActionData, ActionSurveillanceInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: MissionEnvActionData) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
  }

  const handleSubmitOverride = async (
    value?: ActionSurveillanceInput,
    errors?: FormikErrors<ActionSurveillanceInput>
  ) => {
    handleSubmit(value, errors, onSubmit)
  }

  return {
    errors,
    initValue,
    handleSubmit: handleSubmitOverride
  }
}
