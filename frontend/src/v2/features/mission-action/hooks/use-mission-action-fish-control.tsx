import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionFishActionData } from '../../common/types/mission-action'
import { ActionFishControlInput } from '../types/action-type'

export function useMissionActionFishControl(
  action: MissionAction,
  onChange: (newAction: MissionAction, debounceTime?: number) => Promise<unknown>,
  isMissionFinished?: boolean
): AbstractFormikSubFormHook<ActionFishControlInput> {
  const { getCoords } = useCoordinate()
  const value = action?.data as MissionFishActionData
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: MissionFishActionData): ActionFishControlInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    return {
      ...data,
      dates: [startDate, endDate],
      isMissionFinished: !!isMissionFinished,
      geoCoords: getCoords(data.latitude, data.longitude)
    }
  }

  const fromInputToFieldValue = (value: ActionFishControlInput): MissionFishActionData => {
    const { dates, geoCoords, isMissionFinished, ...newData } = value
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    return { ...newData, startDateTimeUtc, endDateTimeUtc }
  }

  const { initValue, handleSubmit, errors } = useAbstractFormik<MissionFishActionData, ActionFishControlInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: MissionFishActionData) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
  }

  const handleSubmitOverride = async (
    value?: ActionFishControlInput,
    errors?: FormikErrors<ActionFishControlInput>
  ) => {
    handleSubmit(value, errors, onSubmit)
  }

  return {
    errors,
    initValue,
    handleSubmit: handleSubmitOverride
  }
}
