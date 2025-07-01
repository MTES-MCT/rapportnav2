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
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()
  const isMissionFinished = useMissionFinished(action.missionId)

  const fromFieldValueToInput = (data: MissionNavActionData): ActionRescueInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    const rescueType = data.isVesselRescue ? RescueType.VESSEL : RescueType.PERSON
    return {
      ...data,
      rescueType,
      dates: [startDate, endDate],
      isMissionFinished: isMissionFinished,
      geoCoords: getCoords(data.latitude, data.longitude)
    }
  }

  const fromInputToFieldValue = (value: ActionRescueInput): MissionNavActionData => {
    const { dates, geoCoords, isMissionFinished, ...newData } = value
    const latitude = geoCoords[0]
    const longitude = geoCoords[1]

    const endDateTimeUtc = postprocessDateFromPicker(dates[1])
    const startDateTimeUtc = postprocessDateFromPicker(dates[0])
    const isVesselRescue = value.rescueType === RescueType.VESSEL
    const isPersonRescue = value.rescueType === RescueType.PERSON

    return {
      ...newData,
      startDateTimeUtc,
      endDateTimeUtc,
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

  const createValidationSchema = (isMissionFinished: boolean) => {
    return object().shape({
      ...getDateRangeSchema(isMissionFinished),
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

  const validationSchema = useMemo(() => createValidationSchema(isMissionFinished), [isMissionFinished])

  return {
    errors,
    initValue,
    validationSchema,
    handleSubmit: handleSubmitOverride
  }
}
