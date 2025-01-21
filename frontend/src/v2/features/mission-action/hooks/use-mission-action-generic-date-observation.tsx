import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction } from '../../common/types/mission-action'
import { MissionActionData } from '../../common/types/mission-action-data'
import { ActionGenericDateObservationInput } from '../types/action-type'

export function useMissionActionGenericDateObservation(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>
): AbstractFormikSubFormHook<ActionGenericDateObservationInput> {
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: MissionActionData): ActionGenericDateObservationInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    return { ...data, startDateTimeUtc: startDate.toISOString(), endDateTimeUtc: endDate.toISOString() }
  }

  const fromInputToFieldValue = (value: ActionGenericDateObservationInput): MissionActionData => {
    const { endDateTimeUtc, startDateTimeUtc, ...newData } = value
    const processedEndDateTimeUtc = postprocessDateFromPicker(endDateTimeUtc)
    const processedStartDateTimeUtc = postprocessDateFromPicker(startDateTimeUtc)
    return { ...newData, startDateTimeUtc: processedStartDateTimeUtc, endDateTimeUtc: processedEndDateTimeUtc }
  }

  const { initValue, handleSubmit, isError } = useAbstractFormik<MissionActionData, ActionGenericDateObservationInput>(
    action.data,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: MissionActionData) => {
    debugger
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
