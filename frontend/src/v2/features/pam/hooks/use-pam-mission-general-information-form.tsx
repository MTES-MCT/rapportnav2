import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form.tsx'
import { MissionGeneralInfo2 } from '../../common/types/mission-types.ts'
import { useDate } from '../../common/hooks/use-date.tsx'
import { array, number, object, string } from 'yup'
import getDateRangeSchema from '../../common/schemas/dates-schema.ts'
import conditionallyRequired from '../../common/schemas/conditionally-required-helper.ts'
import { useMemo } from 'react'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'
import { FormikErrors } from 'formik'

export type MissionPAMGeneralInfoInitialInput = { dates: (Date | undefined)[] } & MissionGeneralInfo2

export const usePamMissionGeneralInfoForm = (
  onChange: (newGeneralInfo: MissionGeneralInfo2) => Promise<unknown>,
  value: MissionGeneralInfo2
) => {
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()
  const isMissionFinished = useMissionFinished(value.missionId)

  const fromFieldValueToInput = (data: MissionGeneralInfo2) => {
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    return {
      ...data,
      dates: [startDate, endDate]
    }
  }

  const fromInputToFieldValue = (value: MissionPAMGeneralInfoInitialInput): MissionGeneralInfo2 => {
    const { dates, ...newValues } = value
    return {
      ...newValues,
      startDateTimeUtc: postprocessDateFromPicker(dates[0]),
      endDateTimeUtc: postprocessDateFromPicker(dates[1])
    }
  }

  const { initValue, handleSubmit, errors } = useAbstractFormik<MissionGeneralInfo2, MissionPAMGeneralInfoInitialInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue,
    []
  )

  const onSubmit = async (valueToSubmit?: MissionGeneralInfo2) => {
    if (!valueToSubmit) return
    await onChange(valueToSubmit)
  }

  const handleSubmitOverride = async (value?: MissionGeneralInfo2, errors?: FormikErrors<MissionGeneralInfo2>) => {
    handleSubmit(value, errors, onSubmit)
  }

  const createValidationSchema = (isMissionFinished: boolean) => {
    return object().shape({
      ...getDateRangeSchema(isMissionFinished),

      distanceInNauticalMiles: conditionallyRequired(
        () => number().nullable(),
        [],
        true,
        'Distance is required when mission is finished',
        schema => schema.nonNullable().required()
      )(isMissionFinished),

      consumedGOInLiters: conditionallyRequired(
        () => number().nullable(),
        [],
        true,
        'GO consumption is required when mission is finished',
        schema => schema.nonNullable().required()
      )(isMissionFinished),

      consumedFuelInLiters: conditionallyRequired(
        () => number().nullable(),
        [],
        true,
        'Fuel consumption is required when mission is finished',
        schema => schema.nonNullable().required()
      )(isMissionFinished),

      nbrOfRecognizedVessel: conditionallyRequired(
        () => number().nullable(),
        [],
        true,
        'Number of recognized vessels is required when mission is finished',
        schema => schema.nonNullable().required()
      )(isMissionFinished),

      observations: conditionallyRequired(
        () => string().nullable(),
        [],
        true,
        'Observations are required',
        schema => schema.nonNullable().required()
      )(isMissionFinished),

      crew: conditionallyRequired(
        () => array().min(1, 'At least one crew member is required when mission is finished').nullable(),
        [],
        true,
        'At least one crew member is required',
        schema => schema.nonNullable().required()
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
