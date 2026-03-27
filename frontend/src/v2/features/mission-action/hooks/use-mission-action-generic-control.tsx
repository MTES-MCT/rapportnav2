import { useMemo } from 'react'
import { object, ObjectShape } from 'yup'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import getDateRangeSchema from '../../common/schemas/dates-schema'
import getGeoCoordsSchema from '../../common/schemas/geocoords-schema'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionNavActionData } from '../../common/types/mission-action'
import { ActionControlInput } from '../types/action-type'
import { useMissionDates } from '../../common/hooks/use-mission-dates.tsx'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'

export function useMissionActionGenericControl(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>,
  schema?: ObjectShape,
  withGeoCoords?: boolean,
  booleans?: string[]
): AbstractFormikSubFormHook<ActionControlInput> {
  const { getCoords } = useCoordinate()
  const { getDateRangeForInput, getDateRangeFromInput } = useDate()
  const isMissionFinished = useMissionFinished(action.ownerId ?? action.missionId)
  const missionDates = useMissionDates(action.ownerId ?? action.missionId)

  const fromFieldValueToInput = (data: MissionNavActionData): ActionControlInput => {
    const dates = getDateRangeForInput(data)
    return {
      ...data,
      dates,
      geoCoords: getCoords(data.latitude, data.longitude)
    }
  }

  const fromInputToFieldValue = (value: ActionControlInput): MissionNavActionData => {
    const { dates, geoCoords, ...newData } = value
    const latitude = geoCoords[0]
    const longitude = geoCoords[1]
    return { ...newData, latitude, longitude, ...getDateRangeFromInput(dates) }
  }

  const { initValue, handleSubmit } = useAbstractFormik<MissionNavActionData, ActionControlInput>(
    action?.data,
    fromFieldValueToInput,
    fromInputToFieldValue,
    booleans
  )

  const onSubmit = async (valueToSubmit?: MissionNavActionData) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
  }

  const handleSubmitOverride = async (value?: ActionControlInput) => {
    handleSubmit(value, onSubmit)
  }

  const createValidationSchema = (isMissionFinished?: boolean, missionStartDate?: string, missionEndDate?: string) => {
    return object().shape({
      ...(withGeoCoords ? getGeoCoordsSchema(isMissionFinished) : {}),
      ...(getDateRangeSchema({
        isMissionFinished,
        missionStartDate,
        missionEndDate
      }) as Record<string, any>),
      ...(schema ?? {})
    })
  }

  const validationSchema = useMemo(
    () => createValidationSchema(isMissionFinished, missionDates.startDateTimeUtc, missionDates.endDateTimeUtc),
    [isMissionFinished, missionDates.startDateTimeUtc, missionDates.endDateTimeUtc]
  )

  return {
    initValue,
    validationSchema,
    handleSubmit: handleSubmitOverride
  }
}
