import { Action, ActionAntiPollution } from '@common/types/action-types'
import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { ActionAntiPollutionInput } from '../types/action-type'

export function useMissionActionAntiPollution(
  action: Action,
  onChange: (newAction: Action) => Promise<unknown>
): AbstractFormikSubFormHook<ActionAntiPollutionInput> {
  const value = action?.data as unknown as ActionAntiPollution
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: ActionAntiPollution): ActionAntiPollutionInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    return { ...data, dates: [startDate, endDate], geoCoords: [data.latitude, data.longitude] }
  }

  const fromInputToFieldValue = (value: ActionAntiPollutionInput): ActionAntiPollution => {
    const { dates, geoCoords, ...newData } = value
    const latitude = geoCoords[0]
    const longitude = geoCoords[1]
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    return { ...newData, startDateTimeUtc, endDateTimeUtc, longitude, latitude }
  }

  const { initValue, handleSubmit, isError } = useAbstractFormik<ActionAntiPollution, ActionAntiPollutionInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue,
    [
      'detectedPollution',
      'diversionCarriedOut',
      'isAntiPolDeviceDeployed',
      'isSimpleBrewingOperationDone',
      'pollutionObservedByAuthorizedAgent'
    ]
  )

  const onSubmit = async (valueToSubmit?: ActionAntiPollution) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: [valueToSubmit] })
  }

  const handleSubmitOverride = async (
    value?: ActionAntiPollutionInput,
    errors?: FormikErrors<ActionAntiPollutionInput>
  ) => {
    handleSubmit(value, errors, onSubmit)
  }

  return {
    isError,
    initValue,
    handleSubmit: handleSubmitOverride
  }
}
