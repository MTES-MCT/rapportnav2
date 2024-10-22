import { ControlGensDeMer, ControlType } from '@common/types/control-types'
import { FieldProps } from 'formik'
import { ControlHook } from '../types/control-hook'
import { useAbstractControl } from './use-abstract-control'
import { useControlRegistry } from './use-control-registry'

const radios = [
  { name: 'staffOutnumbered', label: 'Décision d’effectif conforme au nombre de personnes à bord' },
  { name: 'upToDateMedicalCheck', label: 'Aptitudes médicales ; Visites médicales à jour' },
  {
    name: 'knowledgeOfFrenchLawAndLanguage',
    label: 'Connaissance suffisante de la langue et de la loi français (navires fr/esp)',
    extra: true
  }
]

export type ControlGensDeMerInput = {} & ControlGensDeMer

export function useControlGensDeMer(
  name: string,
  fieldFormik: FieldProps<ControlGensDeMer>
): ControlHook<ControlGensDeMerInput> {
  const { getControlType } = useControlRegistry()
  const { initValue, handleSubmit } = useAbstractControl<ControlGensDeMer, ControlGensDeMerInput>(
    name,
    fieldFormik,
    (input: ControlGensDeMer) => input as ControlGensDeMerInput,
    (value?: ControlGensDeMerInput) => value as ControlGensDeMer
  )

  return {
    radios,
    initValue,
    handleSubmit,
    getValidationSchema: () => {},
    controlTypeLabel: getControlType(ControlType.GENS_DE_MER)
  }
}
