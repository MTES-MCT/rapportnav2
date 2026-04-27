import { useMemo } from 'react'
import { object, ObjectShape } from 'yup'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'
import getDateRangeSchema from '../../common/schemas/dates-schema'
import { cleanLocationFields } from '../../common/schemas/location-fields-cleaner'
import getLocationSchema from '../../common/schemas/location-schema'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionNavActionData } from '../../common/types/mission-action'
import { ActionControlInput } from '../types/action-type'

export function useMissionActionGenericControl(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>,
  schema?: ObjectShape,
  withGeoCoords?: boolean,
  booleans?: string[]
): AbstractFormikSubFormHook<ActionControlInput> {
  const { getCoords } = useCoordinate()
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()
  const isMissionFinished = useMissionFinished(action.ownerId ?? action.missionId)

  const fromFieldValueToInput = (data: MissionNavActionData): ActionControlInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    return {
      ...data,
      dates: [startDate, endDate],
      geoCoords: getCoords(data.latitude, data.longitude)
    }
  }

  const fromInputToFieldValue = (value: ActionControlInput): MissionNavActionData => {
    const { dates, geoCoords, locationType, ...newData } = value
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    if (withGeoCoords) {
      return { ...newData, ...cleanLocationFields(locationType, geoCoords, newData), locationType, startDateTimeUtc, endDateTimeUtc }
    }
    return { ...newData, latitude: geoCoords[0], longitude: geoCoords[1], startDateTimeUtc, endDateTimeUtc }
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

  const createValidationSchema = (isMissionFinished?: boolean) => {
    return object().shape({
      ...(withGeoCoords ? getLocationSchema(isMissionFinished) : {}),
      ...(getDateRangeSchema(isMissionFinished) as Record<string, any>),
      ...(schema ?? {})
    })
  }

  const validationSchema = useMemo(() => createValidationSchema(isMissionFinished), [isMissionFinished])

  return {
    initValue,
    validationSchema,
    handleSubmit: handleSubmitOverride
  }
}
