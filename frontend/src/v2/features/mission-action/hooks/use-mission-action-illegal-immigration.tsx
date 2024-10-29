import { Action, ActionIllegalImmigration } from '@common/types/action-types'
import { FormikErrors } from 'formik'
import { boolean, number, object } from 'yup'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { ActionIllegalImmigrationInput } from '../types/action-type'

export function useMissionActionIllegalImmigration(
  action: Action,
  onChange: (newAction: Action) => Promise<unknown>,
  isMissionFinished?: boolean
): AbstractFormikSubFormHook<ActionIllegalImmigrationInput> {
  const value = action?.data as unknown as ActionIllegalImmigration
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: ActionIllegalImmigration): ActionIllegalImmigrationInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    return {
      ...data,
      dates: [startDate, endDate],
      isMissionFinished: !!isMissionFinished,
      geoCoords: [data.latitude, data.longitude]
    }
  }

  const fromInputToFieldValue = (value: ActionIllegalImmigrationInput): ActionIllegalImmigration => {
    const { dates, geoCoords, isMissionFinished, ...newData } = value
    const latitude = geoCoords[0]
    const longitude = geoCoords[1]
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    return { ...newData, startDateTimeUtc, endDateTimeUtc, longitude, latitude }
  }

  const { initValue, handleSubmit, isError } = useAbstractFormik<
    ActionIllegalImmigration,
    ActionIllegalImmigrationInput
  >(value, fromFieldValueToInput, fromInputToFieldValue)

  const onSubmit = async (valueToSubmit?: ActionIllegalImmigration) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: [valueToSubmit] })
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
