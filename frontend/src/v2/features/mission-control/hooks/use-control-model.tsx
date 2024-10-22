import { ControlNavigation, ControlSecurity, ControlType } from '@common/types/control-types'
import { FieldProps } from 'formik'
import { ControlHook } from '../types/control-hook'
import { useAbstractControl } from './use-abstract-control'
import { useControlRegistry } from './use-control-registry'

type ControlModel = ControlNavigation | ControlSecurity

export type ControlModelInput = {} & ControlModel

export function useControlModel(
  name: string,
  fieldFormik: FieldProps<ControlModel>,
  controlType: ControlType
): ControlHook<ControlModelInput> {
  const { getControlType } = useControlRegistry()
  const { initValue, handleSubmit } = useAbstractControl<ControlModel, ControlModelInput>(
    name,
    fieldFormik,
    (input: ControlModel) => input as ControlModelInput,
    (value?: ControlModelInput) => ({}) as ControlModel
  )

  return {
    initValue,
    handleSubmit,
    getValidationSchema: () => {},
    controlTypeLabel: getControlType(controlType)
  }
}
