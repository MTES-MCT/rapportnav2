import { VesselSizeEnum } from '@common/types/env-mission-types'
import { mixed, object, string } from 'yup'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'
import conditionallyRequired from '../../common/schemas/conditionally-required-helper.ts'
import getDateRangeSchema from '../../common/schemas/dates-schema.ts'
import { cleanLocationFields } from '../../common/schemas/location-fields-cleaner.ts'
import getLocationSchema from '../../common/schemas/location-schema.ts'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionNavActionData } from '../../common/types/mission-action'
import { ActionNavControlInput } from '../types/action-type'
import { useMemo } from 'react'

export function useMissionActionNavControl(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>
): AbstractFormikSubFormHook<ActionNavControlInput> {
  const { getCoords } = useCoordinate()
  const value = action?.data as MissionNavActionData
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()
  const isMissionFinished = useMissionFinished(action.ownerId ?? action.missionId)

  const fromFieldValueToInput = (data: MissionNavActionData): ActionNavControlInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    return {
      ...data,
      dates: [startDate, endDate],
      geoCoords: getCoords(data.latitude, data.longitude)
    }
  }

  const fromInputToFieldValue = (value: ActionNavControlInput): MissionNavActionData => {
    const { dates, geoCoords, locationType, ...newData } = value
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    return { ...newData, ...cleanLocationFields(locationType, geoCoords, newData), locationType, startDateTimeUtc, endDateTimeUtc }
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

  const createValidationSchema = (isMissionFinished: boolean) => {
    return object().shape({
      ...getDateRangeSchema(isMissionFinished),
      ...getLocationSchema(isMissionFinished),

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

  const validationSchema = useMemo(() => createValidationSchema(isMissionFinished), [isMissionFinished])

  return {
    initValue,
    validationSchema,
    handleSubmit: handleSubmitOverride
  }
}
