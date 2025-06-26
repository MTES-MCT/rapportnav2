import { ControlType } from '@common/types/control-types'
import { FieldProps } from 'formik'
import { number, object } from 'yup'
import { Control } from '../../common/types/target-types'
import { AbstractControlFormikHook } from '../types/control-hook'
import { useAbstractControl } from './use-abstract-control'
import { useControlRegistry } from './use-control-registry'

export type ControlEnvInput = {} & Control

export function useEnvControl(
  name: string,
  fieldFormik: FieldProps<Control>,
  controlType: ControlType,
  maxControl?: number
): AbstractControlFormikHook<ControlEnvInput> {
  const { getControlType } = useControlRegistry()
  const { errors, initValue, handleSubmit } = useAbstractControl<Control, ControlEnvInput>(
    name,
    fieldFormik,
    (input: Control) => input as ControlEnvInput,
    (value?: ControlEnvInput) => value as Control
  )

  const getValidationSchema = (maxAmountOfControls?: number) =>
    object().shape({
      amountOfControls: number().max(maxAmountOfControls ?? 1)
    })

  return {
    errors,
    initValue,
    handleSubmit,
    validationSchema: getValidationSchema(maxControl),
    controlTypeLabel: getControlType(controlType)
  }
}
