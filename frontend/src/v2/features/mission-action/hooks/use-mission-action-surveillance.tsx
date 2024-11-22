import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionActionOutput } from '../../common/types/mission-action-output'
import { MissionEnvActionDataOutput } from '../../common/types/mission-env-action-output'
import { ActionSurveillanceInput } from '../types/action-type'

export function useMissionActionSurveillance(
  action: MissionActionOutput,
  onChange: (newAction: MissionActionOutput) => Promise<unknown>
): AbstractFormikSubFormHook<ActionSurveillanceInput> {
  const value = action?.data as unknown as MissionEnvActionDataOutput
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: MissionEnvActionDataOutput): ActionSurveillanceInput => {
    const endDate = preprocessDateForPicker(data?.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    return {
      ...data,
      dates: [startDate, endDate]
    }
  }

  const fromInputToFieldValue = (value: ActionSurveillanceInput): MissionEnvActionDataOutput => {
    const { dates, ...newData } = value
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    return { ...newData, startDateTimeUtc, endDateTimeUtc }
  }

  const { initValue, handleSubmit, isError } = useAbstractFormik<MissionEnvActionDataOutput, ActionSurveillanceInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: MissionEnvActionDataOutput) => {
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
    isError,
    initValue,
    handleSubmit: handleSubmitOverride
  }
}
