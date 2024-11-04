import { Action, ActionFreeNote } from '@common/types/action-types'
import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { ActionFreeNoteInput } from '../types/action-type'

export function useMissionActionFreeNote(
  action: Action,
  onChange: (newAction: Action) => Promise<unknown>
): AbstractFormikSubFormHook<ActionFreeNoteInput> {
  const value = action?.data as unknown as ActionFreeNote
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: ActionFreeNote): ActionFreeNoteInput => {
    return { ...data, date: preprocessDateForPicker(data.startDateTimeUtc) }
  }

  const fromInputToFieldValue = (value: ActionFreeNoteInput): ActionFreeNote => {
    const { date, ...newData } = value
    const startDateTimeUtc = postprocessDateFromPicker(date)
    return { ...newData, startDateTimeUtc }
  }

  const { initValue, handleSubmit, isError } = useAbstractFormik<ActionFreeNote, ActionFreeNoteInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: ActionFreeNote) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: [valueToSubmit] })
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
