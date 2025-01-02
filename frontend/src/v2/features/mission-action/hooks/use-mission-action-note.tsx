import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionNavActionData } from '../../common/types/mission-action'
import { ActionFreeNoteInput } from '../types/action-type'

export function useMissionActionFreeNote(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>
): AbstractFormikSubFormHook<ActionFreeNoteInput> {
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

  const { initValue, handleSubmit, isError } = useAbstractFormik<MissionNavActionData, ActionFreeNoteInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: MissionNavActionData) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
  }

  const handleSubmitOverride = async (value?: ActionFreeNoteInput, errors?: FormikErrors<ActionFreeNoteInput>) => {
    handleSubmit(value, errors, onSubmit)
  }

  return {
    isError,
    initValue,
    handleSubmit: handleSubmitOverride
  }
}
