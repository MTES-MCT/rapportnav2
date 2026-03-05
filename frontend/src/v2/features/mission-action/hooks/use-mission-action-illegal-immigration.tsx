import { FormikErrors } from 'formik'
import { number, object } from 'yup'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionNavActionData } from '../../common/types/mission-action'
import { ActionIllegalImmigrationInput } from '../types/action-type'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'
import { useMissionDates } from '../../common/hooks/use-mission-dates.tsx'
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
  const { getDateRangeForInput, getDateRangeFromInput } = useDate()
  const isMissionFinished = useMissionFinished(action.ownerId ?? action.missionId)
  const missionDates = useMissionDates(action.ownerId ?? action.missionId)

  const fromFieldValueToInput = (data: MissionNavActionData): ActionIllegalImmigrationInput => {
    const dates = getDateRangeForInput(data)
    return {
      ...data,
      dates,
      geoCoords: getCoords(data.latitude, data.longitude)
    }
  }

  const fromInputToFieldValue = (value: ActionIllegalImmigrationInput): MissionNavActionData => {
    const { dates, geoCoords, ...newData } = value
    const latitude = geoCoords[0]
    const longitude = geoCoords[1]
    return { ...newData, ...getDateRangeFromInput(dates), longitude, latitude }
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

  const createValidationSchema = (isMissionFinished: boolean, missionStartDate?: string, missionEndDate?: string) => {
    return object().shape({
      ...getDateRangeSchema({ isMissionFinished, missionStartDate, missionEndDate }),
      ...getGeoCoordsSchema(isMissionFinished),

      nbOfInterceptedVessels: conditionallyRequired(
        () => number().min(0).nullable(),
        [], // No dependencies — the condition is global
        true, // only apply if `isMissionFinished === true`
        'Ce champ est requis quand la mission est terminée',
        schema =>
          schema // Add `.nonNullable()` to enrich the schema
            .nonNullable()
      )(isMissionFinished),

      nbOfInterceptedMigrants: conditionallyRequired(
        () => number().min(0).nullable(),
        [],
        true,
        'Ce champ est requis quand la mission est terminée',
        schema => schema.nonNullable()
      )(isMissionFinished),

      nbOfSuspectedSmugglers: conditionallyRequired(
        () => number().min(0).nullable(),
        [],
        true,
        'Ce champ est requis quand la mission est terminée',
        schema => schema.nonNullable()
      )(isMissionFinished)
    })
  }

  const validationSchema = useMemo(
    () => createValidationSchema(isMissionFinished, missionDates.startDateTimeUtc, missionDates.endDateTimeUtc),
    [isMissionFinished, missionDates.startDateTimeUtc, missionDates.endDateTimeUtc]
  )

  return {
    errors,
    initValue,
    validationSchema,
    handleSubmit: handleSubmitOverride
  }
}
