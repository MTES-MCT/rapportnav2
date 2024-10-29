import { Action } from '@common/types/action-types'
import { EnvActionControl } from '@common/types/env-mission-types'
import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { ActionEnvControlInput } from '../types/action-type'

export function useMissionActionEnvControl(
  action: Action,
  onChange: (newAction: Action) => Promise<unknown>,
  isMissionFinished?: boolean
): AbstractFormikSubFormHook<ActionEnvControlInput> {
  const value = action?.data as unknown as EnvActionControl
  const { extractLatLngFromMultiPoint } = useCoordinate()
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: EnvActionControl): ActionEnvControlInput => {
    const endDate = preprocessDateForPicker(action.endDateTimeUtc)
    const startDate = preprocessDateForPicker(action.startDateTimeUtc)
    return {
      ...data,
      dates: [startDate, endDate],
      isMissionFinished: !!isMissionFinished,
      geoCoords: extractLatLngFromMultiPoint(data.geom)
    }
  }

  const fromInputToFieldValue = (value: ActionEnvControlInput): EnvActionControl => {
    const { dates, geoCoords, isMissionFinished, ...newData } = value
    const actionEndDateTimeUtc = postprocessDateFromPicker(dates[1])
    const actionStartDateTimeUtc = postprocessDateFromPicker(dates[0])
    return { ...newData, actionEndDateTimeUtc, actionStartDateTimeUtc } //TODO: Check EnvActionControl to put start/endDate into data
  }

  const { initValue, handleSubmit, isError } = useAbstractFormik<EnvActionControl, ActionEnvControlInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: EnvActionControl) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: [valueToSubmit] }) //TODO: Update Action to use data instead of array data Action = {... data: {}}
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
