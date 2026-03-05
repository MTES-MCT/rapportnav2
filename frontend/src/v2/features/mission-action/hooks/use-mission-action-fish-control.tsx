import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionFishActionData } from '../../common/types/mission-action'
import { ActionFishControlInput } from '../types/action-type'
import { useMissionDates } from '../../common/hooks/use-mission-dates.tsx'
import { object, string } from 'yup'
import getDateRangeSchema from '../../common/schemas/dates-schema.ts'
import { useMemo } from 'react'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'

export function useMissionActionFishControl(
  action: MissionAction,
  onChange: (newAction: MissionAction, debounceTime?: number) => Promise<unknown>
): AbstractFormikSubFormHook<ActionFishControlInput> {
  const { getCoords } = useCoordinate()
  const value = action?.data as MissionFishActionData
  const { getDateRangeForInput, getDateRangeFromInput } = useDate()
  const isMissionFinished = useMissionFinished(action.missionId)
  const missionDates = useMissionDates(action.missionId)

  const fromFieldValueToInput = (data: MissionFishActionData): ActionFishControlInput => {
    const dates = getDateRangeForInput(data)
    return {
      ...data,
      dates,
      isMissionFinished,
      geoCoords: getCoords(data.latitude, data.longitude)
    }
  }

  const fromInputToFieldValue = (value: ActionFishControlInput): MissionFishActionData => {
    const { dates, geoCoords, isMissionFinished, ...newData } = value
    return { ...newData, ...getDateRangeFromInput(dates) }
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

  const createValidationSchema = (isMissionFinished: boolean, missionStartDate?: string, missionEndDate?: string) => {
    return object().shape({
      ...getDateRangeSchema({ isMissionFinished, missionStartDate, missionEndDate }),
      observationsByUnit: string().nullable()
    })
  }

  const validationSchema = useMemo(
    () => createValidationSchema(isMissionFinished, missionDates.startDateTimeUtc, missionDates.endDateTimeUtc),
    [isMissionFinished, missionDates.startDateTimeUtc, missionDates.endDateTimeUtc]
  )

  return {
    errors,
    initValue,
    validationSchema,
    handleSubmit: handleSubmitOverride
  }
}
