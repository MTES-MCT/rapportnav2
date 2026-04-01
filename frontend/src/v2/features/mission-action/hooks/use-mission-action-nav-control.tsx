import { VesselSizeEnum } from '@common/types/env-mission-types'
import { array, mixed, number, object, string } from 'yup'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'
import conditionallyRequired from '../../common/schemas/conditionally-required-helper.ts'
import getDateRangeSchema from '../../common/schemas/dates-schema.ts'
import getGeoCoordsSchema from '../../common/schemas/geocoords-schema.ts'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { LocationType } from '../../common/types/location-type'
import { MissionAction, MissionNavActionData } from '../../common/types/mission-action'
import { ActionNavControlInput } from '../types/action-type'
import { useMemo, useRef } from 'react'

export function useMissionActionNavControl(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>
): AbstractFormikSubFormHook<ActionNavControlInput> {
  const { getCoords } = useCoordinate()
  const value = action?.data as MissionNavActionData
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()
  const isMissionFinished = useMissionFinished(action.ownerId ?? action.missionId)
  const previousLocationType = useRef<LocationType | undefined>(value?.locationType)

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

    // Check if locationType changed
    const locationTypeChanged = locationType !== previousLocationType.current
    previousLocationType.current = locationType

    // Clear irrelevant fields based on locationType
    if (locationType === LocationType.GPS) {
      return {
        ...newData,
        locationType,
        startDateTimeUtc,
        endDateTimeUtc,
        latitude: geoCoords[0] ?? undefined,
        longitude: geoCoords[1] ?? undefined,
        locationDescription: undefined
      }
    } else {
      // PORT or COMMUNE - clear geoCoords and locationDescription if locationType changed
      return {
        ...newData,
        locationType,
        startDateTimeUtc,
        endDateTimeUtc,
        latitude: undefined,
        longitude: undefined,
        locationDescription: locationTypeChanged ? undefined : newData.locationDescription
      }
    }
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

      locationType: conditionallyRequired(
        () => mixed<LocationType>().nullable().oneOf(Object.values(LocationType)),
        [],
        true,
        'Location type is required',
        schema => schema.nonNullable()
      )(isMissionFinished),

      geoCoords: mixed().when('locationType', {
        is: LocationType.GPS,
        then: () => getGeoCoordsSchema(isMissionFinished).geoCoords,
        otherwise: () => array().of(number()).nullable()
      }),

      portLocode: string().when('locationType', {
        is: (val: LocationType) => val === LocationType.PORT,
        then: () => (isMissionFinished ? string().required('Location description is required') : string().nullable()),
        otherwise: () => string().nullable()
      }),

      zipCode: string().when('locationType', {
        is: (val: LocationType) => val === LocationType.COMMUNE,
        then: () => (isMissionFinished ? string().required('Location description is required') : string().nullable()),
        otherwise: () => string().nullable()
      }),

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
