import { FormikErrors } from 'formik'
import { array, boolean, date, number, object } from 'yup'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useCoordinate } from '../../common/hooks/use-coordinate'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionActionOutput } from '../../common/types/mission-action-output'
import { MissionNavActionDataOutput } from '../../common/types/mission-nav-action-output'
import { RescueType } from '../../common/types/rescue-type'
import { ActionRescueInput } from '../types/action-type'

export function useMissionActionRescue(
  action: MissionActionOutput,
  onChange: (newAction: MissionActionOutput) => Promise<unknown>,
  isMissionFinished?: boolean
): AbstractFormikSubFormHook<ActionRescueInput> {
  const { getCoords } = useCoordinate()
  const value = action?.data as MissionNavActionDataOutput
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: MissionNavActionDataOutput): ActionRescueInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    const rescueType = data.isVesselRescue ? RescueType.VESSEL : RescueType.PERSON
    return {
      ...data,
      rescueType,
      dates: [startDate, endDate],
      isMissionFinished: !!isMissionFinished,
      geoCoords: getCoords(data.latitude, data.longitude)
    }
  }

  const fromInputToFieldValue = (value: ActionRescueInput): MissionNavActionDataOutput => {
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

  const { initValue, handleSubmit, isError } = useAbstractFormik<MissionNavActionDataOutput, ActionRescueInput>(
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

  const onSubmit = async (valueToSubmit?: MissionNavActionDataOutput) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
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
