import { Action } from '@common/types/action-types'
import { FishAction } from '@common/types/fish-mission-types'
import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { ActionFishControlInput } from '../types/action-type'

export function useMissionActionFishControl(
  action: Action,
  onChange: (newAction: Action) => Promise<unknown>,
  isMissionFinished?: boolean
): AbstractFormikSubFormHook<ActionFishControlInput> {
  const value = action?.data as unknown as FishAction
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: FishAction): ActionFishControlInput => {
    const endDate = preprocessDateForPicker(action.endDateTimeUtc)
    const startDate = preprocessDateForPicker(action.startDateTimeUtc)
    return {
      ...data,
      dates: [startDate, endDate],
      isMissionFinished: !!isMissionFinished,
      geoCoords: [data.latitude, data.longitude]
    }
  }

  const fromInputToFieldValue = (value: ActionFishControlInput): FishAction => {
    const { dates, geoCoords, isMissionFinished, ...newData } = value
    const actionEndDatetimeUtc = postprocessDateFromPicker(dates[1])
    const actionDatetimeUtc = postprocessDateFromPicker(dates[0])
    return { ...newData, actionDatetimeUtc, actionEndDatetimeUtc }
  }

  const { initValue, handleSubmit, isError } = useAbstractFormik<FishAction, ActionFishControlInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: FishAction) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: [valueToSubmit] }) //TODO: Update Action to use data instead of array data Action = {... data: {}}
  }

  const handleSubmitOverride = async (
    value?: ActionFishControlInput,
    errors?: FormikErrors<ActionFishControlInput>
  ) => {
    handleSubmit(value, errors, onSubmit)
  }

  return {
    isError,
    initValue,
    handleSubmit: handleSubmitOverride
  }
}
