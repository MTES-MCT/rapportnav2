import { ControlType } from '@common/types/control-types'
import { FieldProps } from 'formik'
import { Control } from '../../common/types/target-types'
import { AbstractControlFormikHook } from '../types/control-hook'
import { useAbstractControl } from './use-abstract-control'
import { useControlRegistry } from './use-control-registry'

export type ControlInput = {} & Control

export function useControl(
  name: string,
  fieldFormik: FieldProps<Control>,
  controlType: ControlType
): AbstractControlFormikHook<ControlInput> & { withRadios: boolean } {
  const { getRadios, getEmptyValues, getControlType } = useControlRegistry()
  const { initValue, handleSubmit } = useAbstractControl<Control, ControlInput>(
    name,
    fieldFormik,
    (input: Control) => input as ControlInput,
    (value?: ControlInput) => {
      return (!value ? value : { ...value, ...(!value?.hasBeenDone ? getEmptyValues(controlType) : {}) }) as Control
    }
  )

  return {
    initValue,
    handleSubmit,
    radios: getRadios(controlType),
    controlTypeLabel: getControlType(controlType),
    withRadios: [ControlType.GENS_DE_MER, ControlType.ADMINISTRATIVE].includes(controlType)
  }
}
