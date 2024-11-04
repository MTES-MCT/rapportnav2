import { Action, ActionRescue } from '@common/types/action-types'
import { FormikErrors } from 'formik'
import { array, boolean, date, number, object } from 'yup'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { RescueType } from '../../common/types/rescue-type'
import { ActionRescueInput } from '../types/action-type'

export function useMissionActionRescue(
  action: Action,
  onChange: (newAction: Action) => Promise<unknown>,
  isMissionFinished?: boolean
): AbstractFormikSubFormHook<ActionRescueInput> {
  const value = action?.data as unknown as ActionRescue
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: ActionRescue): ActionRescueInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    const rescueType = data.isVesselRescue ? RescueType.VESSEL : RescueType.PERSON
    return {
      ...data,
      rescueType,
      dates: [startDate, endDate],
      isMissionFinished: !!isMissionFinished,
      geoCoords: [data.latitude ?? 0, data.longitude ?? 0]
    }
  }

  const fromInputToFieldValue = (value: ActionRescueInput): ActionRescue => {
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

  const { initValue, handleSubmit, isError } = useAbstractFormik<ActionRescue, ActionRescueInput>(
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

  const onSubmit = async (valueToSubmit?: ActionRescue) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: [valueToSubmit] })
  }

  const handleSubmitOverride = async (value?: ActionRescueInput, errors?: FormikErrors<ActionRescueInput>) => {
    handleSubmit(value, errors, onSubmit)
  }

  const validationSchema = object().shape({
    isMissionFinished: boolean(),
    isPersonRescue: boolean(),
    isMigrationRescue: boolean(),
    dates: array().of(date()).length(2),
    geoCoords: array().of(number()).length(2),
    numberOfPersonRescued: number()
      .nullable()
      .when(['isPersonRescue', 'isMissionFinished'], {
        is: true,
        then: schema => schema.nonNullable().required(),
        otherwise: schema => schema.nullable()
      }),
    numberOfDeaths: number()
      .nullable()
      .when(['isPersonRescue', 'isMissionFinished'], {
        is: true,
        then: schema => schema.nonNullable().required(),
        otherwise: schema => schema.nullable()
      }),
    nbOfVesselsTrackedWithoutIntervention: number()
      .nullable()
      .when(['isMigrationRescue', 'isMissionFinished'], {
        is: true,
        then: schema => schema.nonNullable().required(),
        otherwise: schema => schema.nullable()
      }),
    nbAssistedVesselsReturningToShore: number()
      .nullable()
      .when(['isMigrationRescue', 'isMissionFinished'], {
        is: true,
        then: schema => schema.nonNullable().required(),
        otherwise: schema => schema.nullable()
      })
  })

  return {
    isError,
    initValue,
    validationSchema,
    handleSubmit: handleSubmitOverride
  }
}
