import { VesselSizeEnum } from '@common/types/env-mission-types'
import { FormikErrors } from 'formik'
import { boolean, mixed, object, string } from 'yup'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionNavActionData } from '../../common/types/mission-action'
import { ActionNavControlInput } from '../types/action-type'

export function useMissionActionNavControl(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>,
  isMissionFinished?: boolean
): AbstractFormikSubFormHook<ActionNavControlInput> {
  const { getCoords } = useCoordinate()
  const value = action?.data as MissionNavActionData
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: MissionNavActionData): ActionNavControlInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    return {
      ...data,
      dates: [startDate, endDate],
      isMissionFinished: !!isMissionFinished,
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

  const { initValue, handleSubmit, isError } = useAbstractFormik<MissionNavActionData, ActionNavControlInput>(
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

  const validationSchema = object().shape({
    isMissionFinished: boolean(),
    vesselSize: mixed<VesselSizeEnum>()
      .nullable()
      .oneOf(Object.values(VesselSizeEnum))
      .when('isMissionFinished', {
        is: true,
        then: schema => schema.nonNullable().required()
      }),
    vesselIdentifier: string()
      .nullable()
      .when('isMissionFinished', {
        is: true,
        then: schema => schema.nonNullable().required()
      }),
    identityControlledPersonValue: string().when('isMissionFinished', {
      is: true,
      then: schema => schema.nonNullable().required()
    })
  })

  return {
    isError,
    initValue,
    validationSchema,
    handleSubmit: handleSubmitOverride
  }
}
