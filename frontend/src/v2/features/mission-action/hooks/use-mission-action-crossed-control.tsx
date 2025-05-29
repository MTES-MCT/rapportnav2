import { useStore } from '@tanstack/react-store'
import { FormikErrors } from 'formik'
import { isNull, omitBy } from 'lodash'
import { date, mixed, object, string } from 'yup'
import { store } from '../../../store'
import { useAbstractFormik } from '../../common/hooks/use-abstract-formik-form'
import { useDate } from '../../common/hooks/use-date'
import { AbstractFormikSubFormHook } from '../../common/types/abstract-formik-hook'
import { CrossControlConclusionType, CrossControlStatusType } from '../../common/types/crossed-control-type'
import { MissionAction, MissionNavActionData } from '../../common/types/mission-action'
import { Target } from '../../common/types/target-types'
import { ActionCrossControlInput } from '../types/action-type'

export function useMissionActionCrossControl(
  action: MissionAction,
  onChange: (newAction: MissionAction) => Promise<unknown>
): AbstractFormikSubFormHook<ActionCrossControlInput> {
  const user = useStore(store, state => state.user)
  const value = action?.data as MissionNavActionData
  const { preprocessDateForPicker, postprocessDateFromPicker } = useDate()

  const fromFieldValueToInput = (data: MissionNavActionData): ActionCrossControlInput => {
    const endDate = preprocessDateForPicker(data.endDateTimeUtc)
    const startDate = preprocessDateForPicker(data.startDateTimeUtc)
    const crossControlStatus = data.crossControl?.status ?? CrossControlStatusType.NEW
    const crossControl = omitBy(
      {
        ...(data.crossControl ?? {}),
        serviceId: data.crossControl?.serviceId ?? user?.serviceId
      },
      isNull
    )
    return {
      ...data,
      endDate,
      startDate,
      crossControl,
      crossControlStatus
    }
  }

  const fromInputToFieldValue = (value: ActionCrossControlInput): MissionNavActionData => {
    const { endDate, startDate, targets, crossControlStatus, ...newData } = value

    const conclusion = newData.crossControl?.conclusion
    const endDateTimeUtc = postprocessDateFromPicker(endDate)
    const startDateTimeUtc = postprocessDateFromPicker(startDate)

    return {
      ...newData,
      startDateTimeUtc,
      endDateTimeUtc,
      crossControl: {
        ...newData.crossControl,
        status: crossControlStatus
      },
      targets: conclusion === CrossControlConclusionType.NO_FOLLOW_UP ? withoutControls(targets) : targets
    }
  }

  const { initValue, handleSubmit, isError } = useAbstractFormik<MissionNavActionData, ActionCrossControlInput>(
    value,
    fromFieldValueToInput,
    fromInputToFieldValue,
    ['isSignedByInspector']
  )

  const withoutControls = (targets?: Target[]): Target[] | undefined => {
    targets?.forEach(target =>
      target.controls?.forEach(control => {
        if (control.infractions?.length) control.infractions = []
      })
    )
    return targets
  }

  const onSubmit = async (valueToSubmit?: MissionNavActionData) => {
    if (!valueToSubmit) return
    await onChange({ ...action, data: valueToSubmit })
  }

  const handleSubmitOverride = async (
    value?: ActionCrossControlInput,
    errors?: FormikErrors<ActionCrossControlInput>
  ) => {
    handleSubmit(value, errors, onSubmit)
  }

  const validationSchema = object().shape({
    crossControl: object().shape({
      id: string().required(),
      conclusion: mixed<CrossControlConclusionType>()
        .oneOf(Object.values(CrossControlConclusionType))
        .required('Veuillez indiquer les conclusions du contrôle pour pouvoir le clôturer')
    }),
    endDate: date().required('Veuillez indiquer une date de fin pour clôturer le contrôle')
  })

  return {
    isError,
    initValue,
    validationSchema,
    handleSubmit: handleSubmitOverride
  }
}
