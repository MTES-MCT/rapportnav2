import { VesselSizeEnum } from '@common/types/env-mission-types'
import { FormikErrors } from 'formik'
import { boolean, mixed, object, string } from 'yup'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionActionOutput } from '../../common/types/mission-action-output'
import { MissionNavActionDataOutput } from '../../common/types/mission-nav-action-output'
import { ActionNavControlInput } from '../types/action-type'

export function useMissionActionNavControl(
  action: MissionActionOutput,
  onChange: (newAction: MissionActionOutput) => Promise<unknown>,
  isMissionFinished?: boolean
): AbstractFormikSubFormHook<ActionNavControlInput> {
  const value = action?.data as MissionNavActionDataOutput
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: MissionNavActionDataOutput): ActionNavControlInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    return {
      ...data,
      dates: [startDate, endDate],
      isMissionFinished: !!isMissionFinished,
      geoCoords: [data.latitude, data.longitude]
    }
  }

  const fromInputToFieldValue = (value: ActionNavControlInput): MissionNavActionDataOutput => {
    const { dates, isMissionFinished, geoCoords, ...newData } = value
    const latitude = geoCoords[0] ?? 0
    const longitude = geoCoords[1] ?? 0
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    return { ...newData, startDateTimeUtc, endDateTimeUtc, longitude, latitude }
  }

  const { initValue, handleSubmit, isError } = useAbstractFormik<MissionNavActionDataOutput, ActionNavControlInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: MissionNavActionDataOutput) => {
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
