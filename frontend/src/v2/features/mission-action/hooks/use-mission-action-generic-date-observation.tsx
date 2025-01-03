import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionActionDataOutput, MissionActionOutput } from '../../common/types/mission-action-output'
import { ActionGenericDateObservationInput } from '../types/action-type'

export function useMissionActionGenericDateObservation(
  action: MissionActionOutput,
  onChange: (newAction: MissionActionOutput) => Promise<unknown>
): AbstractFormikSubFormHook<ActionGenericDateObservationInput> {
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: MissionActionDataOutput): ActionGenericDateObservationInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    return { ...data, dates: [startDate, endDate] }
  }

  const fromInputToFieldValue = (value: ActionGenericDateObservationInput): MissionActionDataOutput => {
    const { dates, ...newData } = value
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    return { ...newData, startDateTimeUtc, endDateTimeUtc }
  }

  const { initValue, handleSubmit, isError } = useAbstractFormik<
    MissionActionDataOutput,
    ActionGenericDateObservationInput
  >(action.data, fromFieldValueToInput, fromInputToFieldValue)

  const onSubmit = async (valueToSubmit?: MissionActionDataOutput) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
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
