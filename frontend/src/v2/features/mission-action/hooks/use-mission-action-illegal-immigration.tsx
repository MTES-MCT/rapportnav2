import { FormikErrors } from 'formik'
import { number, object } from 'yup'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionNavActionData } from '../../common/types/mission-action'
import { ActionIllegalImmigrationInput } from '../types/action-type'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'
import { useMemo } from 'react'
import getDateRangeSchema from '../../common/schemas/dates-schema.ts'
import getGeoCoordsSchema from '../../common/schemas/geocoords-schema.ts'
import conditionallyRequired from '../../common/schemas/conditionally-required-helper.ts'

export function useMissionActionIllegalImmigration(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>
): AbstractFormikSubFormHook<ActionIllegalImmigrationInput> {
  const { getCoords } = useCoordinate()
  const value = action?.data as MissionNavActionData
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()
  const isMissionFinished = useMissionFinished(action.missionId)

  const fromFieldValueToInput = (data: MissionNavActionData): ActionIllegalImmigrationInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    return {
      ...data,
      dates: [startDate, endDate],
      isMissionFinished: isMissionFinished,
      geoCoords: getCoords(data.latitude, data.longitude)
    }
  }

  const fromInputToFieldValue = (value: ActionIllegalImmigrationInput): MissionNavActionData => {
    const { dates, geoCoords, isMissionFinished, ...newData } = value
    const latitude = geoCoords[0]
    const longitude = geoCoords[1]
    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    return { ...newData, startDateTimeUtc, endDateTimeUtc, longitude, latitude }
  }

  const { initValue, handleSubmit, errors } = useAbstractFormik<MissionNavActionData, ActionIllegalImmigrationInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: MissionNavActionData) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
  }

  const handleSubmitOverride = async (
    value?: ActionIllegalImmigrationInput,
    errors?: FormikErrors<ActionIllegalImmigrationInput>
  ) => {
    await handleSubmit(value, errors, onSubmit)
  }

  const createValidationSchema = (isMissionFinished: boolean) => {
    return object().shape({
      ...getDateRangeSchema(isMissionFinished),
      ...getGeoCoordsSchema(isMissionFinished),

      nbOfInterceptedVessels: conditionallyRequired(
        () => number().nullable(),
        [], // No dependencies — the condition is global
        true, // only apply if `isMissionFinished === true`
        'Ce champ est requis quand la mission est terminée',
        schema =>
          schema // Add `.nonNullable()` to enrich the schema
            .nonNullable()
      )(isMissionFinished),

      nbOfInterceptedMigrants: conditionallyRequired(
        () => number().nullable(),
        [],
        true,
        'Ce champ est requis quand la mission est terminée',
        schema => schema.nonNullable()
      )(isMissionFinished),

      nbOfSuspectedSmugglers: conditionallyRequired(
        () => number().nullable(),
        [],
        true,
        'Ce champ est requis quand la mission est terminée',
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
