import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionNavActionData } from '../../common/types/mission-action'
import { ActionFreeNoteInput } from '../types/action-type'
import { object } from 'yup'
import { useMemo } from 'react'
import { useMissionDates } from '../../common/hooks/use-mission-dates.tsx'
import { useMissionFinished } from '../../common/hooks/use-mission-finished.tsx'
import { getSingleDateSchema } from '../../common/schemas/dates-schema.ts'

export function useMissionActionFreeNote(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>
): AbstractFormikSubFormHook<ActionFreeNoteInput> {
  const missionDates = useMissionDates(action.ownerId ?? action.missionId)
  const isMissionFinished = useMissionFinished(action.ownerId ?? action.missionId)
  const value = action?.data as MissionNavActionData
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: MissionNavActionData): ActionFreeNoteInput => {
    return { ...data, date: preprocessDateForPicker(data.startDateTimeUtc) }
  }

  const fromInputToFieldValue = (value: ActionFreeNoteInput): MissionNavActionData => {
    const { date, ...newData } = value
    const startDateTimeUtc = postprocessDateFromPicker(date)
    return { ...newData, startDateTimeUtc }
  }

  const { initValue, handleSubmit } = useAbstractFormik<MissionNavActionData, ActionFreeNoteInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: MissionNavActionData) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
  }

  const handleSubmitOverride = async (value?: ActionFreeNoteInput) => {
    handleSubmit(value, onSubmit)
  }

  const createValidationSchema = (isMissionFinished: boolean, missionStartDate?: string, missionEndDate?: string) => {
    return object().shape({
      date: getSingleDateSchema({ isMissionFinished, missionStartDate, missionEndDate })
    })
  }

  const validationSchema = useMemo(
    () => createValidationSchema(isMissionFinished, missionDates.startDateTimeUtc, missionDates.endDateTimeUtc),
    [isMissionFinished, missionDates.startDateTimeUtc, missionDates.endDateTimeUtc]
  )

  return {
    initValue,
    validationSchema,
    handleSubmit: handleSubmitOverride
  }
}
