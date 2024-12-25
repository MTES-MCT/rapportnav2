import { FormikErrors } from 'formik'
import { boolean, number, object } from 'yup'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionActionOutput } from '../../common/types/mission-action-output'
import { MissionNavActionDataOutput } from '../../common/types/mission-nav-action-output'
import { ActionIllegalImmigrationInput } from '../types/action-type'

export function useMissionActionIllegalImmigration(
  action: MissionActionOutput,
  onChange: (newAction: MissionActionOutput) => Promise<unknown>,
  isMissionFinished?: boolean
): AbstractFormikSubFormHook<ActionIllegalImmigrationInput> {
  const { getCoords } = useCoordinate()
  const value = action?.data as MissionNavActionDataOutput
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: MissionNavActionDataOutput): ActionIllegalImmigrationInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    return {
      ...data,
      dates: [startDate, endDate],
      isMissionFinished: !!isMissionFinished,
      geoCoords: getCoords(data.latitude, data.longitude)
    }
  }

  const fromInputToFieldValue = (value: ActionIllegalImmigrationInput): MissionNavActionDataOutput => {
    const { dates, geoCoords, isMissionFinished, ...newData } = value
    const latitude = geoCoords[0]
    const longitude = geoCoords[1]
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    return { ...newData, startDateTimeUtc, endDateTimeUtc, longitude, latitude }
  }

  const { initValue, handleSubmit, isError } = useAbstractFormik<
    MissionNavActionDataOutput,
    ActionIllegalImmigrationInput
  >(value, fromFieldValueToInput, fromInputToFieldValue)

  const onSubmit = async (valueToSubmit?: MissionNavActionDataOutput) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
  }

  const handleSubmitOverride = async (
    value?: ActionIllegalImmigrationInput,
    errors?: FormikErrors<ActionIllegalImmigrationInput>
  ) => {
    handleSubmit(value, errors, onSubmit)
  }

  const validationSchema = object().shape({
    isMissionFinished: boolean(),
    nbOfInterceptedVessels: number()
      .nullable()
      .when('isMissionFinished', {
        is: true,
        then: schema => schema.nonNullable().required()
      }),
    nbOfInterceptedMigrants: number()
      .nullable()
      .when('isMissionFinished', {
        is: true,
        then: schema => schema.nonNullable().required()
      }),
    nbOfSuspectedSmugglers: number()
      .nullable()
      .when('isMissionFinished', {
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
