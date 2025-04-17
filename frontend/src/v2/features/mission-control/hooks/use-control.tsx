import { ControlType } from '@common/types/control-types'
import { FieldProps } from 'formik'
import { Control } from '../../common/types/target-types'
import { AbstractControlFormikHook } from '../types/control-hook'
import { useAbstractControl } from './use-abstract-control'
import { useControlRegistry } from './use-control-registry'

export type ControlModelInput = {} & Control

export function useControl(
  name: string,
  fieldFormik: FieldProps<Control>,
  controlType: ControlType
): AbstractControlFormikHook<ControlModelInput> & { withRadios: boolean } {
  const { getRadios, getEmptyValues, getControlType } = useControlRegistry()
  const { initValue, handleSubmit } = useAbstractControl<Control, ControlModelInput>(
    name,
    fieldFormik,
    (input: Control) => input as ControlModelInput,
    (value?: ControlModelInput) => {
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
