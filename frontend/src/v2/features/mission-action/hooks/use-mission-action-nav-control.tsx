import { VesselSizeEnum } from '@common/types/env-mission-types'
import { mixed, object, string } from 'yup'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionNavActionData } from '../../common/types/mission-action'
import { ActionNavControlInput } from '../types/action-type'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'
import { useMissionDates } from '../../common/hooks/use-mission-dates.tsx'
import getDateRangeSchema from '../../common/schemas/dates-schema.ts'
import getGeoCoordsSchema from '../../common/schemas/geocoords-schema.ts'
import conditionallyRequired from '../../common/schemas/conditionally-required-helper.ts'
import { useMemo } from 'react'

export function useMissionActionNavControl(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>
): AbstractFormikSubFormHook<ActionNavControlInput> {
  const { getCoords } = useCoordinate()
  const value = action?.data as MissionNavActionData
  const { getDateRangeForInput, getDateRangeFromInput } = useDate()
  const isMissionFinished = useMissionFinished(action.ownerId ?? action.missionId)
  const missionDates = useMissionDates(action.ownerId ?? action.missionId)

  const fromFieldValueToInput = (data: MissionNavActionData): ActionNavControlInput => {
    const dates = getDateRangeForInput(data)
    return {
      ...data,
      dates,
      geoCoords: getCoords(data.latitude, data.longitude)
    }
  }

  const fromInputToFieldValue = (value: ActionNavControlInput): MissionNavActionData => {
    const { dates, geoCoords, ...newData } = value
    const latitude = geoCoords[0] ?? 0
    const longitude = geoCoords[1] ?? 0
    return { ...newData, ...getDateRangeFromInput(dates), longitude, latitude }
  }

  const { initValue, handleSubmit } = useAbstractFormik<MissionNavActionData, ActionNavControlInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: MissionNavActionData) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
  }

  const handleSubmitOverride = async (value?: ActionNavControlInput) => {
    handleSubmit(value, onSubmit)
  }

  const createValidationSchema = (isMissionFinished: boolean, missionStartDate?: string, missionEndDate?: string) => {
    return object().shape({
      ...getDateRangeSchema({ isMissionFinished, missionStartDate, missionEndDate }),
      ...getGeoCoordsSchema(isMissionFinished),

      vesselSize: conditionallyRequired(
        () => mixed<VesselSizeEnum>().nullable().oneOf(Object.values(VesselSizeEnum)).default(undefined),
        [],
        true,
        'Vessel size is required when the mission is finished',
        schema => schema.nonNullable()
      )(isMissionFinished),

      vesselIdentifier: conditionallyRequired(
        () => string().nullable().default(undefined),
        [],
        true,
        'Vessel identifier is required when the mission is finished',
        schema => schema.nonNullable()
      )(isMissionFinished),

      identityControlledPerson: conditionallyRequired(
        () => string().nullable().default(undefined),
        [],
        true,
        'Identity controlled person is required when the mission is finished',
        schema => schema.nonNullable()
      )(isMissionFinished)
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
