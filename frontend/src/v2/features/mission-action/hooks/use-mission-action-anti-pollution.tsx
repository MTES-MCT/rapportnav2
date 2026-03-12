import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionNavActionData } from '../../common/types/mission-action'
import { ActionAntiPollutionInput } from '../types/action-type'
import { object } from 'yup'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'
import getGeoCoordsSchema from '../../common/schemas/geocoords-schema.ts'
import { useMemo } from 'react'
import { useMissionDates } from '../../common/hooks/use-mission-dates.tsx'
import getDateRangeSchema from '../../common/schemas/dates-schema.ts'

export function useMissionActionAntiPollution(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>
): AbstractFormikSubFormHook<ActionAntiPollutionInput> {
  const { getCoords } = useCoordinate()
  const value = action?.data as MissionNavActionData
  const { getDateRangeForInput, getDateRangeFromInput } = useDate()
  const isMissionFinished = useMissionFinished(action.ownerId ?? action.missionId)
  const missionDates = useMissionDates(action.ownerId ?? action.missionId)

  const fromFieldValueToInput = (data: MissionNavActionData): ActionAntiPollutionInput => {
    const dates = getDateRangeForInput(data)
    return {
      ...data,
      dates,
      isMissionFinished,
      geoCoords: getCoords(data.latitude, data.longitude)
    }
  }

  const fromInputToFieldValue = (value: ActionAntiPollutionInput): MissionNavActionData => {
    const { dates, geoCoords, ...newData } = value
    const latitude = geoCoords[0]
    const longitude = geoCoords[1]
    return { ...newData, ...getDateRangeFromInput(dates), longitude, latitude }
  }

  const { initValue, handleSubmit, errors } = useAbstractFormik<MissionNavActionData, ActionAntiPollutionInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue,
    [
      'detectedPollution',
      'diversionCarriedOut',
      'isAntiPolDeviceDeployed',
      'isSimpleBrewingOperationDone',
      'pollutionObservedByAuthorizedAgent'
    ]
  )

  const onSubmit = async (valueToSubmit?: MissionNavActionData) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
  }

  const handleSubmitOverride = async (
    value?: ActionAntiPollutionInput,
    errors?: FormikErrors<ActionAntiPollutionInput>
  ) => {
    handleSubmit(value, errors, onSubmit)
  }

  const createValidationSchema = (isMissionFinished: boolean, missionStartDate?: string, missionEndDate?: string) => {
    return object().shape({
      ...getDateRangeSchema({ isMissionFinished, missionStartDate, missionEndDate }),
      ...getGeoCoordsSchema(isMissionFinished)
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
