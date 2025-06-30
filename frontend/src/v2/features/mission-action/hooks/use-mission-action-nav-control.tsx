import { VesselSizeEnum } from '@common/types/env-mission-types'
import { FormikErrors } from 'formik'
import { mixed, object, string } from 'yup'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionNavActionData } from '../../common/types/mission-action'
import { ActionNavControlInput } from '../types/action-type'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'
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
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()
  const isMissionFinished = useMissionFinished(action.missionId)

  const fromFieldValueToInput = (data: MissionNavActionData): ActionNavControlInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    return {
      ...data,
      dates: [startDate, endDate],
      isMissionFinished: isMissionFinished,
      geoCoords: getCoords(data.latitude, data.longitude)
    }
  }

  const fromInputToFieldValue = (value: ActionNavControlInput): MissionNavActionData => {
    const { dates, isMissionFinished, geoCoords, ...newData } = value
    const latitude = geoCoords[0] ?? 0
    const longitude = geoCoords[1] ?? 0
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    return { ...newData, startDateTimeUtc, endDateTimeUtc, longitude, latitude }
  }

  const { initValue, handleSubmit, errors } = useAbstractFormik<MissionNavActionData, ActionNavControlInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: MissionNavActionData) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
  }

  const handleSubmitOverride = async (value?: ActionNavControlInput, errors?: FormikErrors<ActionNavControlInput>) => {
    handleSubmit(value, errors, onSubmit)
  }

  const createValidationSchema = (isMissionFinished: boolean) => {
    return object().shape({
      ...getDateRangeSchema(isMissionFinished),
      ...getGeoCoordsSchema(isMissionFinished),

      vesselSize: conditionallyRequired(
        () => mixed<VesselSizeEnum>().nullable().oneOf(Object.values(VesselSizeEnum)).default(undefined),
        'isMissionFinished',
        true,
        'Vessel size is required when the mission is finished',
        schema => schema.nonNullable()
      )(isMissionFinished),

      vesselIdentifier: conditionallyRequired(
        () => string().nullable().default(undefined),
        'isMissionFinished',
        true,
        'Vessel identifier is required when the mission is finished',
        schema => schema.nonNullable()
      )(isMissionFinished),

      identityControlledPerson: conditionallyRequired(
        () => string().nullable().default(undefined),
        'isMissionFinished',
        true,
        'Identity controlled person is required when the mission is finished',
        schema => schema.nonNullable()
      )(isMissionFinished)
    })
  }

  const validationSchema = useMemo(() => createValidationSchema(isMissionFinished), [isMissionFinished])

  return {
    errors,
    initValue,
    validationSchema,
    handleSubmit: handleSubmitOverride
  }
}
