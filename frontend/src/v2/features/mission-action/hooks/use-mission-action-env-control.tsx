import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionActionOutput } from '../../common/types/mission-action-output'
import { MissionEnvActionDataOutput } from '../../common/types/mission-env-action-output'
import { ActionEnvControlInput } from '../types/action-type'

export function useMissionActionEnvControl(
  action: MissionActionOutput,
  onChange: (newAction: MissionActionOutput) => Promise<unknown>,
  isMissionFinished?: boolean
): AbstractFormikSubFormHook<ActionEnvControlInput> {
  const value = action?.data as MissionEnvActionDataOutput
  const { extractLatLngFromMultiPoint } = useCoordinate()
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: MissionEnvActionDataOutput): ActionEnvControlInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    return {
      ...data,
      dates: [startDate, endDate],
      isMissionFinished: !!isMissionFinished,
      geoCoords: extractLatLngFromMultiPoint(data.geom)
    }
  }

  const fromInputToFieldValue = (value: ActionEnvControlInput): MissionEnvActionDataOutput => {
    const { dates, geoCoords, isMissionFinished, ...newData } = value
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    return { ...newData, endDateTimeUtc, startDateTimeUtc }
  }

  const { initValue, handleSubmit, isError } = useAbstractFormik<MissionEnvActionDataOutput, ActionEnvControlInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: MissionEnvActionDataOutput) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
  }

  const handleSubmitOverride = async (value?: ActionEnvControlInput, errors?: FormikErrors<ActionEnvControlInput>) => {
    handleSubmit(value, errors, onSubmit)
  }

  return {
    isError,
    initValue,
    handleSubmit: handleSubmitOverride
  }
}
