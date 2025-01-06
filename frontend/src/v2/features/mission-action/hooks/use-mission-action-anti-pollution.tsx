import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionNavActionData } from '../../common/types/mission-action'
import { ActionAntiPollutionInput } from '../types/action-type'

export function useMissionActionAntiPollution(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>
): AbstractFormikSubFormHook<ActionAntiPollutionInput> {
  const { getCoords } = useCoordinate()
  const value = action?.data as MissionNavActionData
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: MissionNavActionData): ActionAntiPollutionInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    return { ...data, dates: [startDate, endDate], geoCoords: getCoords(data.latitude, data.longitude) }
  }

  const fromInputToFieldValue = (value: ActionAntiPollutionInput): MissionNavActionData => {
    const { dates, geoCoords, ...newData } = value
    const latitude = geoCoords[0]
    const longitude = geoCoords[1]
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    return { ...newData, startDateTimeUtc, endDateTimeUtc, longitude, latitude }
  }

  const { initValue, handleSubmit, isError } = useAbstractFormik<MissionNavActionData, ActionAntiPollutionInput>(
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

  const onSubmit = async (valueToSubmit?: MissionNavActionData) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
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
