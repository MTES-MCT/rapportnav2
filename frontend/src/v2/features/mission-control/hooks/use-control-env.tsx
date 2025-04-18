import {
  ControlAdministrative,
  ControlGensDeMer,
  ControlNavigation,
  ControlSecurity,
  ControlType
} from '@common/types/control-types'
import { FieldProps } from 'formik'
import { number, object } from 'yup'
import { Control } from '../../common/types/target-types'
import { AbstractControlFormikHook } from '../types/control-hook'
import { useAbstractControl } from './use-abstract-control'
import { useControlRegistry } from './use-control-registry'

export type EnvControl = ControlAdministrative | ControlGensDeMer | ControlNavigation | ControlSecurity | Control
export type EnvControlInput = {} & EnvControl

export function useEnvControl(
  name: string,
  fieldFormik: FieldProps<EnvControl>,
  controlType: ControlType,
  maxControl?: number
): AbstractControlFormikHook<EnvControlInput> {
  const { getControlType } = useControlRegistry()
  const { isError, initValue, handleSubmit } = useAbstractControl<EnvControlInput, EnvControlInput>(
    name,
    fieldFormik,
    (input: EnvControl) => input as EnvControlInput,
    (value?: EnvControlInput) => value as EnvControl
  )

  const getValidationSchema = (maxAmountOfControls?: number) =>
    object().shape({
      amountOfControls: number().max(maxAmountOfControls ?? 1)
    })

  return {
    isError,
    initValue,
    handleSubmit,
    validationSchema: getValidationSchema(maxControl),
    controlTypeLabel: getControlType(controlType)
  }
}
