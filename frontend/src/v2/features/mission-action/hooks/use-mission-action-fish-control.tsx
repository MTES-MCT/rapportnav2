import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionActionOutput } from '../../common/types/mission-action-output'
import { MissionFishActionDataOutput } from '../../common/types/mission-fish-action-output'
import { ActionFishControlInput } from '../types/action-type'

export function useMissionActionFishControl(
  action: MissionActionOutput,
  onChange: (newAction: MissionActionOutput, debounceTime?: number) => Promise<unknown>,
  isMissionFinished?: boolean
): AbstractFormikSubFormHook<ActionFishControlInput> {
  const value = action?.data as MissionFishActionDataOutput
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: MissionFishActionDataOutput): ActionFishControlInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    return {
      ...data,
      dates: [startDate, endDate],
      isMissionFinished: !!isMissionFinished,
      geoCoords: [data.latitude, data.longitude] //TODO: Check coords 2 number after the comma unless, it will trigger a diff
    }
  }

  const fromInputToFieldValue = (value: ActionFishControlInput): MissionFishActionDataOutput => {
    const { dates, geoCoords, isMissionFinished, ...newData } = value
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    return { ...newData, startDateTimeUtc, endDateTimeUtc }
  }

  const { initValue, handleSubmit, isError } = useAbstractFormik<MissionFishActionDataOutput, ActionFishControlInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: MissionFishActionDataOutput) => {
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
    isError,
    initValue,
    handleSubmit: handleSubmitOverride
  }
}
