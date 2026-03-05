import { FormikErrors } from 'formik'
import { boolean, number, object } from 'yup'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionNavActionData } from '../../common/types/mission-action'
import { RescueType } from '../../common/types/rescue-type'
import { ActionRescueInput } from '../types/action-type'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'
import { useMissionDates } from '../../common/hooks/use-mission-dates.tsx'
import getDateRangeSchema from '../../common/schemas/dates-schema.ts'
import getGeoCoordsSchema from '../../common/schemas/geocoords-schema.ts'
import { useMemo } from 'react'
import conditionallyRequired from '../../common/schemas/conditionally-required-helper.ts'

export function useMissionActionRescue(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>
): AbstractFormikSubFormHook<ActionRescueInput> {
  const { getCoords } = useCoordinate()
  const value = action?.data as MissionNavActionData
  const { getDateRangeForInput, getDateRangeFromInput } = useDate()
  const isMissionFinished = useMissionFinished(action.missionId)
  const missionDates = useMissionDates(action.missionId)

  const fromFieldValueToInput = (data: MissionNavActionData): ActionRescueInput => {
    const dates = getDateRangeForInput(data)
    const rescueType = data.isVesselRescue ? RescueType.VESSEL : RescueType.PERSON
    return {
      ...data,
      rescueType,
      dates,
      isMissionFinished: isMissionFinished,
      geoCoords: getCoords(data.latitude, data.longitude)
    }
  }

  const fromInputToFieldValue = (value: ActionRescueInput): MissionNavActionData => {
    const { dates, geoCoords, isMissionFinished, ...newData } = value
    const latitude = geoCoords[0]
    const longitude = geoCoords[1]
    const isVesselRescue = value.rescueType === RescueType.VESSEL
    const isPersonRescue = value.rescueType === RescueType.PERSON
    return {
      ...newData,
      ...getDateRangeFromInput(dates),
      longitude,
      latitude,
      isVesselRescue,
      isPersonRescue
    }
  }

  const { initValue, handleSubmit, errors } = useAbstractFormik<MissionNavActionData, ActionRescueInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue,
    [
      'isVesselRescue',
      'isVesselTowed',
      'isPersonRescue',
      'isVesselNoticed',
      'isMigrationRescue',
      'operationFollowsDEFREP',
      'isInSRRorFollowedByCROSSMRCC'
    ]
  )

  const onSubmit = async (valueToSubmit?: MissionNavActionData) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
  }

  const handleSubmitOverride = async (value?: ActionRescueInput, errors?: FormikErrors<ActionRescueInput>) => {
    handleSubmit(value, errors, onSubmit)
  }

  const createValidationSchema = (isMissionFinished: boolean, missionStartDate?: string, missionEndDate?: string) => {
    return object().shape({
      ...getDateRangeSchema({ isMissionFinished, missionStartDate, missionEndDate }),
      ...getGeoCoordsSchema(isMissionFinished),
      isPersonRescue: boolean(),
      isMigrationRescue: boolean(),

      // Fields related to isPersonRescue
      numberPersonsRescued: conditionallyRequired(
        () => number().min(0).nullable(),
        ['isPersonRescue'],
        true,
        'numberPersonsRescued is required'
      )(isMissionFinished),
      numberOfDeaths: conditionallyRequired(
        () => number().min(0).nullable(),
        ['isPersonRescue'],
        true,
        'numberOfDeaths is required'
      )(isMissionFinished),

      // Fields related to isMigrationRescue
      nbOfVesselsTrackedWithoutIntervention: conditionallyRequired(
        () => number().min(0).nullable(),
        ['isMigrationRescue'],
        true,
        'nbOfVesselsTrackedWithoutIntervention is required'
      )(isMissionFinished),
      nbAssistedVesselsReturningToShore: conditionallyRequired(
        () => number().min(0).nullable(),
        ['isMigrationRescue'],
        true,
        'nbAssistedVesselsReturningToShore is required'
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
