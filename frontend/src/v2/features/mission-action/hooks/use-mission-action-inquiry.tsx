import { FormikErrors } from 'formik'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { MissionAction, MissionNavActionData } from '../../common/types/mission-action'
import { ActionInquiryInput } from '../types/action-type'

export function useMissionActionInquiry(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>
): AbstractFormikSubFormHook<ActionInquiryInput> {
  const value = action?.data as MissionNavActionData

  const fromFieldValueToInput = (data: MissionNavActionData): ActionInquiryInput => data
  const fromInputToFieldValue = (value: ActionInquiryInput): MissionNavActionData => value

  const { initValue, handleSubmit } = useAbstractFormik<MissionNavActionData, ActionInquiryInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue
  )

  const onSubmit = async (valueToSubmit?: MissionNavActionData) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
  }

  const handleSubmitOverride = async (value?: ActionInquiryInput, errors?: FormikErrors<ActionInquiryInput>) => {
    handleSubmit(value, errors, onSubmit)
  }

  return {
    initValue,
    handleSubmit: handleSubmitOverride
  }
}
