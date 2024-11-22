import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionActionOutput } from '../../common/types/mission-action-output'
import { MissionNavActionDataOutput } from '../../common/types/mission-nav-action-output'
import { ActionFreeNoteInput } from '../types/action-type'

export function useMissionActionFreeNote(
  action: MissionActionOutput,
  onChange: (newAction: MissionActionOutput) => Promise<unknown>
): AbstractFormikSubFormHook<ActionFreeNoteInput> {
  const value = action?.data as MissionNavActionDataOutput
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: MissionNavActionDataOutput): ActionFreeNoteInput => {
    return { ...data, date: preprocessDateForPicker(data.startDateTimeUtc) }
  }

  const fromInputToFieldValue = (value: ActionFreeNoteInput): MissionNavActionDataOutput => {
    const { date, ...newData } = value
    const startDateTimeUtc = postprocessDateFromPicker(date)
    return { ...newData, startDateTimeUtc }
  }

  const { initValue, handleSubmit, isError } = useAbstractFormik<MissionNavActionDataOutput, ActionFreeNoteInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: MissionNavActionDataOutput) => {
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
