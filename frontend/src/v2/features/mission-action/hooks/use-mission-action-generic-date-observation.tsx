import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction } from '../../common/types/mission-action'
import { MissionActionData } from '../../common/types/mission-action-data'
import { ActionGenericDateObservationInput } from '../types/action-type'
import { object } from 'yup'
import getDateRangeSchema from '../../common/schemas/dates-schema.ts'
import { useMemo } from 'react'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'

export function useMissionActionGenericDateObservation(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>
): AbstractFormikSubFormHook<ActionGenericDateObservationInput> {
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()
  const isMissionFinished = useMissionFinished(action.missionId)

  const fromFieldValueToInput = (data: MissionActionData): ActionGenericDateObservationInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    return { ...data, dates: [startDate, endDate] }
  }

  const fromInputToFieldValue = (value: ActionGenericDateObservationInput): MissionActionData => {
    const { dates, ...newData } = value
    const processedEndDateTimeUtc = postprocessDateFromPicker(dates[1])
    const processedStartDateTimeUtc = postprocessDateFromPicker(dates[0])
    return { ...newData, startDateTimeUtc: processedStartDateTimeUtc, endDateTimeUtc: processedEndDateTimeUtc }
  }

  const { initValue, handleSubmit, errors } = useAbstractFormik<MissionActionData, ActionGenericDateObservationInput>(
    action.data,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: MissionActionData) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
  }

  const handleSubmitOverride = async (
    value?: ActionGenericDateObservationInput,
    errors?: FormikErrors<ActionGenericDateObservationInput>
  ) => {
    handleSubmit(value, errors, onSubmit)
  }

  const createValidationSchema = (isMissionFinished: boolean) => {
    return object().shape({
      ...getDateRangeSchema(isMissionFinished)
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
