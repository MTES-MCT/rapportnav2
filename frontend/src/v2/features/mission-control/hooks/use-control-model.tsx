import { ControlNavigation, ControlSecurity, ControlType } from '@common/types/control-types'
import { FieldProps } from 'formik'
import { AbstractControlFormikHook } from '../types/control-hook'
import { useAbstractControl } from './use-abstract-control'
import { useControlRegistry } from './use-control-registry'

const emptyModel = {
  observations: undefined
}

type ControlModel = ControlNavigation | ControlSecurity

export type ControlModelInput = {} & ControlModel

export function useControlModel(
  name: string,
  fieldFormik: FieldProps<ControlModel>,
  controlType: ControlType
): AbstractControlFormikHook<ControlModelInput> {
  const { getControlType } = useControlRegistry()
  const { initValue, handleSubmit } = useAbstractControl<ControlModel, ControlModelInput>(
    name,
    fieldFormik,
    (input: ControlModel) => input as ControlModelInput,
    (value?: ControlModelInput) => {
      return (!value ? value : { ...value, ...(!value?.hasBeenDone ? emptyModel : {}) }) as ControlModel
    }
  )

  return {
    initValue,
    handleSubmit,
    controlTypeLabel: getControlType(controlType)
  }
}
